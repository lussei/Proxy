package com.proxy1;


import java.net.*;
import java.io.*;
import java.time.Duration;
import java.time.Instant;



public class ProxyThread extends Thread
{
    private Socket socket = null;
    private static final int BUFFER_SIZE = 32768;
    private ProxyLog log;


    public ProxyThread(Socket socket) {
        this.socket = socket;
        log = new ProxyLog();
    }

    public void run() {
        //get input from user
        //send request to server
        //get response from server
        //send response to user

        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

            //Lee la url desde el socket seteado en la main.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine;

            String urlToCall = this.GetURLRequest(bufferedReader);
            int responseResult = 0;
            String info = "";
            Instant inicio =Instant.now();

            BufferedReader urlBufferReader = null;
            try {
                InputStream inputStream = callToUrl(urlToCall);
                SendResponse(dataOutputStream, inputStream);
            } catch (Exception e) {
                //System.err.println("Encountered exception: " + e);
                dataOutputStream.writeBytes("");
            }
            Instant fin = Instant.now();
            double duracion = Duration.between(inicio, fin).getNano()/1000000000.0;
            log.setTime(duracion);
            System.out.println(log.toString());
            closeResources(dataOutputStream, bufferedReader);

        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    private void closeResources(DataOutputStream dataOutputStream, BufferedReader bufferedReader) throws IOException {
        //close out all resources
        //if (urlBufferReader != null) {
        //    urlBufferReader.close();
        //}
        if (dataOutputStream != null) {
            dataOutputStream.close();
        }
        if (bufferedReader != null) {
            bufferedReader.close();
        }
        if (socket != null) {
            socket.close();
        }
    }

    /**
     * llama a la URL y devuelve el stream de salida
     * @param urlToCall
     * @return
     * @throws IOException
     */
    private InputStream callToUrl(String urlToCall) throws IOException {
        URL url = new URL(urlToCall);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.setDoInput(true);
        //not doing HTTP posts
        urlConnection.setDoOutput(false);
        log.setContentType(urlConnection.getContentType());
        log.setLength(urlConnection.getContentLength());

        log.setResult(urlConnection.getResponseCode());
        InputStream inputStream = null;//to get the in stream from url connection
        try {
            inputStream = urlConnection.getInputStream();
        } catch (IOException e) {

        }
        return inputStream;
    }


    /**
     * Busca en el buffer por la url que solicito el browser.
     * @param bufferedReader buffer que se creo desde el socket
     * @return
     * @throws IOException
     */
    private String GetURLRequest(BufferedReader bufferedReader) throws IOException {
        String inputLine;
        int cnt = 0;
        String urlToCall="";
        while ((inputLine = bufferedReader.readLine()) != null) {
            //parse the first line of the request to find the url
            if (cnt == 0) {
                String[] lines = inputLine.split(" ");
                urlToCall = lines[1];
                log.setUrl(urlToCall);
                break;
            }
            cnt++;
        }
        System.out.println("Request for : " + log.getUrl());
        return urlToCall;
    }


    /**
     * Envia la respuesta al output stream creado desde el socket
     * @param dataOutputStream Punto de salida del proxy
     * @param inputStream Flujo proveniente de la url remota
     * @throws IOException
     */
    private void SendResponse(DataOutputStream dataOutputStream, InputStream inputStream) throws IOException {
        byte byteArr[] = new byte[ BUFFER_SIZE ];
        int index = inputStream != null ?  inputStream.read( byteArr, 0, BUFFER_SIZE ) : -1;
        while ( index != -1 )
        {
            dataOutputStream.write( byteArr, 0, index );
            index = inputStream.read( byteArr, 0, BUFFER_SIZE );
        }
        dataOutputStream.flush();
    }
}