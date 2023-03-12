package com.example.se2einzelbsp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.text.InputType;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private EditText inputField;
    private TextView resultField;

    private String hostname = "se2-isys.aau.at";
    private int port = 53212;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputField = (EditText) findViewById(R.id.inputField);
        inputField.setKeyListener(new NumberKeyListener() {
            @Override
            public int getInputType() {
                return InputType.TYPE_CLASS_NUMBER;
            }

            @Override
            protected char[] getAcceptedChars() {
                return "0123456789".toCharArray();
            }
        });

        resultField = (TextView) findViewById(R.id.resultField);

        Button sendButton = (Button) findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputField.getText().toString();
                new SendTask().execute(input);
            }
        });
        Button teiler = (Button) findViewById(R.id.teiler);
        teiler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = inputField.getText().toString();
                checkMatrikelnummer(input);
            }
        });
    }

    private class SendTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String input = params[0];
            try {
                Socket socket = new Socket(hostname, port);
                DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                outToServer.writeBytes(input + '\n');
                String result = inFromServer.readLine();
                socket.close();
                return result;
            } catch (IOException e) {
                return "Error: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            resultField.setText(result);
        }
    }

    public void checkMatrikelnummer(String matrikelnummer) {
        int length = matrikelnummer.length();
        for (int i = 0; i < length - 1; i++) {
            int digit1 = Character.getNumericValue(matrikelnummer.charAt(i));
            for (int j = i + 1; j < length; j++) {
                int digit2 = Character.getNumericValue(matrikelnummer.charAt(j));
                if (hasCommonDiv(digit1, digit2)) {
                    resultField.setText(i + "," + j);
                }else{
                    resultField.setText("Nothing found!");
                }
            }
        }

    }

    private boolean hasCommonDiv(int a, int b) {
        if (a == 0 || b == 0) {
            return false;
        }
        if (a % b == 0 || b % a == 0) {
            return true;
        }
        for (int i = 2; i <= Math.min(a, b); i++) {
            if (a % i == 0 && b % i == 0) {
                return true;
            }
        }
        return false;
    }
}
