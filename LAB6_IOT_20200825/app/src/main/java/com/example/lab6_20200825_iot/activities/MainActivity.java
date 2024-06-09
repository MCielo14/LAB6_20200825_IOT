
package com.example.lab6_20200825_iot.activities;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.lab6_20200825_iot.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.AuthMethodPickerLayout;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.auth.api.credentials.Credentials;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
// RC_SIGN_IN se usa para identificar el inicio de sesión
    private static final int RC_SIGN_IN = 123;
    private final Intent signInIntent;

    public MainActivity() {
        // Se configura los tres tipos de autenticación
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );

        // Se usa una vista de logueo personalizada ya que en el lab de indica que se requiere logo
        AuthMethodPickerLayout customLayout = new AuthMethodPickerLayout
                .Builder(R.layout.login_personalizado)
                .setEmailButtonId(R.id.email_button)
                .setGoogleButtonId(R.id.google_button)
                .setFacebookButtonId(R.id.facebook_button)
                .build();

        signInIntent = AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setAuthMethodPickerLayout(customLayout)
                .setIsSmartLockEnabled(false)
                .setLogo(R.drawable.logo)
                .setTheme(R.style.FirebaseUITheme)
                .build();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Se obtiene una instancia del Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // Se comprueba si hay un usario autenticado previamente
        if (auth.getCurrentUser() != null) {
            auth.signOut();
            Credentials.getClient(this).disableAutoSignIn();
        }
        // Se incia la actividad de inicio de sesión
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Cuano se tiene un inicio de sesión exitoso se le redirige a ListaIngreso
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Intent intent = new Intent(MainActivity.this, ListaIngreso.class);
                startActivity(intent);
                finish();
            } else {
            }
        }
    }
}
