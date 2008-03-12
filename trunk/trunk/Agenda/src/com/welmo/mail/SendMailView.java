package com.welmo.mail;

import android.app.Activity;   
import android.os.Bundle;   
import android.util.Log;   
import android.view.View;   
import android.widget.Button;   
import android.widget.EditText;   
import com.tft.myoffice.R;

public class SendMailView extends Activity {   
    /**  
     * Called with the activity is first created.  
     */  
    @Override  
    public void onCreate(Bundle icicle) {   
        super.onCreate(icicle);   
        setContentView(R.layout.sendmailtestview);   
        final Button send = (Button) this.findViewById(R.id.send);   
        final EditText userid = (EditText) this.findViewById(R.id.userid);   
        final EditText password = (EditText) this.findViewById(R.id.password);   
        final EditText from = (EditText) this.findViewById(R.id.from);   
        final EditText to = (EditText) this.findViewById(R.id.to);   
        final EditText subject = (EditText) this.findViewById(R.id.subject2);   
        final EditText body = (EditText) this.findViewById(R.id.body2);   
        send.setOnClickListener(new View.OnClickListener() {   
            public void onClick(View view) {   
                MailSender sender = new MailSender(userid.getText().toString(), password.getText().toString());   
                try {   
                    sender.sendMail(subject.getText().toString(),   
                            body.getText().toString(),   
                            from.getText().toString(),   
                            to.getText().toString());   
                } catch (Exception e) {   
                    Log.e("SendMail", e.getMessage(), e);   
                }   
            }   
        });   
    }   
}  
