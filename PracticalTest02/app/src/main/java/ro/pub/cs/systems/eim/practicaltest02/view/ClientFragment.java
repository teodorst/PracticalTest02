package ro.pub.cs.systems.eim.practicaltest02.view;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ro.pub.cs.systems.eim.practicaltest02.R;
import ro.pub.cs.systems.eim.practicaltest02.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02.network.ClientCommunicationThread;
import ro.pub.cs.systems.eim.practicaltest02.network.ServerThread;

public class ClientFragment extends Fragment {

    private Handler handler;

    private EditText serverPortEditText;
    private EditText queryParamEditText;
    private Button sendButton;
    private TextView clientResultTextView;
    private ButtonClickListener buttonClickListener = new ButtonClickListener();

    private class ButtonClickListener implements Button.OnClickListener {
        @Override
        public void onClick(View view) {
            String queryParam = queryParamEditText.getText().toString();
            String portString = serverPortEditText.getText().toString();
            if (portString != null && !portString.isEmpty()) {
                int port = Integer.parseInt(portString);
                ClientCommunicationThread clientCommunicationThread = new ClientCommunicationThread(
                        port, handler, queryParam, clientResultTextView);
                clientCommunicationThread.start();
            }
            else {
                Log.d(Constants.TAG, "PORTUL ESTE NULL");
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle state) {
        return inflater.inflate(R.layout.fragment_client, parent, false);
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        handler = new Handler();

        queryParamEditText = (EditText)getActivity().findViewById(R.id.query_param_edit_text);
        sendButton = (Button) getActivity().findViewById(R.id.send_button);

        sendButton.setOnClickListener(buttonClickListener);
        clientResultTextView = (TextView)getActivity().findViewById(R.id.client_result_text);
        serverPortEditText = (EditText) getActivity().findViewById(R.id.server_port_edit_text);
    }


}
