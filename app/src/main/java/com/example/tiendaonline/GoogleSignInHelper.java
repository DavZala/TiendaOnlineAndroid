package com.example.tiendaonline;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class GoogleSignInHelper {

    private static GoogleSignInClient mGoogleSignInClient;

    public static GoogleSignInClient getGoogleSignInClient(Context context) {
        if (mGoogleSignInClient == null) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        }
        return mGoogleSignInClient;
    }

    public static void signOut(Context context) {
        getGoogleSignInClient(context).signOut().addOnCompleteListener(task -> {
            Toast.makeText(context, "Has cerrado sesión", Toast.LENGTH_SHORT).show();
        });
    }
}
