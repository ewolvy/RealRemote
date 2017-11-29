package com.mooo.ewolvy.broadcastdiscovery;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;

/**
******************************************************************************
This library will need some arguments when called with StartActivityForResult
and will return the server selected by the user, if any.

The needed parameters are:
broadcast.port: port number on which the servers are listening
broadcast.service: the service name you are looking for
broadcast.maxtimeout: the maximum time to wait for a response from the servers

The final result will be on the intent:
broadcast.server: the server information selected by the user (if any)
******************************************************************************
*/

public class BroadcastDiscover extends AppCompatActivity {
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";

    String port;        // Must be initialized on starting Activity through Intent
    String service;     // Must be initialized on starting Activity through Intent
    int maxWaitTime;    // Can be initialized on starting Activity, default to 10000

    ArrayList<String> servers = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_discover);

        getServiceAndPort();

        Button btn_search = findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                beginSearch();
            }
        });
    }

    private void getServiceAndPort(){
        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);
        if (extras != null){
            port = extras.getString("broadcast.port", "");
            service = extras.getString("broadcast.service", "NO_SERVICE");
            maxWaitTime = extras.getInt("broadcast.maxtimeout", 10000);
        }else{
            port = "";
            service = "NO_SERVICE";
            maxWaitTime = 10000;
        }
    }

    private void beginSearch(){
        if (service.equals("NO_SERVICE")){
            Toast.makeText(this, "Wrong use of BroadcastDiscover", Toast.LENGTH_SHORT).show();
        }else {
            BroadcastRequest br = new BroadcastRequest(this);
            br.execute(service, port);
        }
    }

    // AsyncTask to find the list of all the available servers
    private class BroadcastRequest extends AsyncTask<String, String, Void>{
        Context context;
        ListView listView;

        BroadcastRequest (Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            listView = findViewById(R.id.lst_view);
            arrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, servers);
            listView.setAdapter(arrayAdapter);
            servers.clear();

            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            findViewById(R.id.btn_search).setEnabled(false);
            if (listView != null) listView.setEnabled(false);
        }

        @Override
        protected Void doInBackground(String... arguments) {
            // arguments must be service on [0] and port on [1]

            final String BROADCAST_IP = "255.255.255.255";

            DatagramSocket datagramSocket;
            // Find the server using UDP broadcast
            try {
                //Open a random port to send the package
                datagramSocket = new DatagramSocket();
                datagramSocket.setBroadcast(true);

                byte[] sendData = arguments[0].getBytes();
                int port = Integer.parseInt(arguments[1]);

                //Try the 255.255.255.255 first
                try {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, InetAddress.getByName(BROADCAST_IP), port);
                    datagramSocket.send(sendPacket);
                    System.out.println(getClass().getName() + ">>> Request packet sent to: 255.255.255.255 (DEFAULT)");
                } catch (Exception e) {
                    Log.d("BROADCAST_DISCOVER", e.toString());
                }

                // Broadcast the message over all the network interfaces (need test if it's really needed, so far it is not)
                /*
                Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = (NetworkInterface) interfaces.nextElement();

                    if (networkInterface.isLoopback() || !networkInterface.isUp()) {
                        continue; // Don't want to broadcast to the loopback interface
                    }

                    for (InterfaceAddress interfaceAddress : networkInterface.getInterfaceAddresses()) {
                        InetAddress broadcast = interfaceAddress.getBroadcast();
                        if (broadcast == null) {
                            continue;
                        }

                        // Send the broadcast package!
                        try {
                            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, broadcast, port);
                            datagramSocket.send(sendPacket);
                        } catch (Exception e) {
                            Log.d("BROADCAST_DISCOVER", e.toString());
                        }

                        System.out.println(getClass().getName() + ">>> Request packet sent to: " + broadcast.getHostAddress() + "; Interface: " + networkInterface.getDisplayName());
                    }
                }

                System.out.println(getClass().getName() + ">>> Done looping over all network interfaces. Now waiting for a reply!");
                */

                //Wait for a response(s)
                ServerSocket serverSocket = new ServerSocket(19103);
                boolean timeoutReached;
                serverSocket.setSoTimeout(5000);
                Socket clientSocket = null;
                String message;
                long startTime= System.currentTimeMillis();

                while (System.currentTimeMillis() < startTime + maxWaitTime) {
                    timeoutReached = false;
                    try {
                        clientSocket = serverSocket.accept();
                    } catch (SocketTimeoutException e) {
                        timeoutReached = true;
                        Log.d("SENDBROADCAST", "Timeout reached!!!");
                    }
                    if (!timeoutReached && clientSocket != null) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        message = in.readLine();
                        clientSocket.close();
                        if (message != null) {
                            publishProgress(message);
                        }
                    }
                }

                if (clientSocket != null) clientSocket.close();
                serverSocket.close();

            } catch (IOException ex) {
                Log.d("SENDBROADCAST", ex.toString());
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            servers.add(values[0]);
            arrayAdapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            findViewById(R.id.btn_search).setEnabled(true);

            if (listView != null) {
                listView.setEnabled(true);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = getIntent();
                        intent.putExtra("broadcast.server", servers.get(i));
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                });
            }
        }
    }
}
