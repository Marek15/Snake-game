package main.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpRequest {


    public static String sendGETRequest( String path){

        try{
            URL url = new URL( path );
            HttpURLConnection httpURLConnection = (HttpURLConnection ) url.openConnection();
            httpURLConnection.setRequestMethod( "GET" );

            InputStreamReader inputStreamReader = new InputStreamReader( httpURLConnection.getInputStream() );
            BufferedReader bufferedReader = new BufferedReader( inputStreamReader );

            StringBuilder response = new StringBuilder();
            String line;
            while ( ( line = bufferedReader.readLine() ) != null ) {
                response.append( line );
            }

            bufferedReader.close();


            if ( response.isEmpty() )
                response.append( "201" );

            return response.toString();
        }
        catch ( Exception e) {
            return "";
        }
    }

    public static int sendPOSTRequest( String path, String postData ) {
        try {

            URL url = new URL( path );
            HttpURLConnection httpURLConnection = ( HttpURLConnection ) url.openConnection();
            httpURLConnection.setRequestMethod( "POST" );
            httpURLConnection.setDoOutput( true );


            // Adding post data
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write( postData.getBytes( StandardCharsets.UTF_8 ) );
            outputStream.flush();
            outputStream.close();

            System.out.println( "POST response code => " + httpURLConnection.getResponseCode() );

            return httpURLConnection.getResponseCode();

        }
        catch ( Exception e) {
            return 500;
        }
    }

}
