package com.example.lab6_20200825_iot.activities;

// Importaciones necesarias
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

    private static final int RC_SIGN_IN = 123;
    private final Intent signInIntent;

    public MainActivity() {
        // Configurar FirebaseUI
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build()
        );

        // Personalizar la pantalla de inicio de sesión
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
                .setIsSmartLockEnabled(false) // Deshabilitar Smart Lock
                .setLogo(R.drawable.logo)  // Aquí pones tu logo
                .setTheme(R.style.FirebaseUITheme)   // Aquí pones tu tema personalizado
                .build();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cierra la sesión si el usuario ya está autenticado
        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            auth.signOut();
            Credentials.getClient(this).disableAutoSignIn();
        }

        // Iniciar la pantalla de inicio de sesión
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                // Inicio de sesión exitoso
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                // Redirigir a la actividad ListaIngreso
                Intent intent = new Intent(MainActivity.this, ListaIngreso.class);
                startActivity(intent);
                finish();
            } else {
                // Maneja el error
            }
        }
    }
}
