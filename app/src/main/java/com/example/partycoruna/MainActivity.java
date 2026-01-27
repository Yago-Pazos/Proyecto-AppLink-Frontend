package com.example.partycoruna;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.partycoruna.fragments.AmigosFragment;
import com.example.partycoruna.fragments.FavoritosFragment;
import com.example.partycoruna.fragments.HomeFragment;
import com.example.partycoruna.fragments.PerfilFragment;

/* MainActivity
 - Actividad principal con soporte edge-to-edge. Manténla como launcher o cambia el manifest.
 */
public class MainActivity extends AppCompatActivity {

    private LinearLayout navHome, navFavoritos, navAmigos, navPerfil;
    private ImageView iconHome, iconFavoritos, iconAmigos, iconPerfil;
    private TextView textHome, textFavoritos, textAmigos, textPerfil;
    private Fragment currentFragment;
    private int currentNavItem = 0; // 0: Home, 1: Favoritos, 2: Amigos, 3: Perfil

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeViews();
        setupNavigation();
        
        // Asegurar que el botón Home esté expandido inicialmente
        navHome.post(() -> {
            textHome.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int textWidth = textHome.getMeasuredWidth();
            int minWidth = (int) (56 * getResources().getDisplayMetrics().density);
            int maxWidth = minWidth + textWidth + (int) (16 * getResources().getDisplayMetrics().density);
            ViewGroup.LayoutParams params = navHome.getLayoutParams();
            params.width = maxWidth;
            navHome.setLayoutParams(params);
            textHome.setAlpha(1f);
        });
        
        loadFragment(new HomeFragment());
    }

    private void initializeViews() {
        navHome = findViewById(R.id.nav_home);
        navFavoritos = findViewById(R.id.nav_favoritos);
        navAmigos = findViewById(R.id.nav_amigos);
        navPerfil = findViewById(R.id.nav_perfil);

        iconHome = findViewById(R.id.icon_home);
        iconFavoritos = findViewById(R.id.icon_favoritos);
        iconAmigos = findViewById(R.id.icon_amigos);
        iconPerfil = findViewById(R.id.icon_perfil);

        textHome = findViewById(R.id.text_home);
        textFavoritos = findViewById(R.id.text_favoritos);
        textAmigos = findViewById(R.id.text_amigos);
        textPerfil = findViewById(R.id.text_perfil);
    }

    private void setupNavigation() {
        navHome.setOnClickListener(v -> switchNavigation(0, new HomeFragment()));
        navFavoritos.setOnClickListener(v -> switchNavigation(1, new FavoritosFragment()));
        navAmigos.setOnClickListener(v -> switchNavigation(2, new AmigosFragment()));
        navPerfil.setOnClickListener(v -> switchNavigation(3, new PerfilFragment()));
    }

    private void switchNavigation(int position, Fragment fragment) {
        if (currentNavItem == position) {
            return; // Ya está seleccionado
        }

        // Desactivar el item anterior
        deactivateNavItem(currentNavItem);

        // Activar el nuevo item
        currentNavItem = position;
        activateNavItem(position);

        // Cambiar el fragmento
        loadFragment(fragment);
    }

    private void activateNavItem(int position) {
        LinearLayout navItem;
        ImageView icon;
        TextView text;

        switch (position) {
            case 0:
                navItem = navHome;
                icon = iconHome;
                text = textHome;
                break;
            case 1:
                navItem = navFavoritos;
                icon = iconFavoritos;
                text = textFavoritos;
                break;
            case 2:
                navItem = navAmigos;
                icon = iconAmigos;
                text = textAmigos;
                break;
            case 3:
                navItem = navPerfil;
                icon = iconPerfil;
                text = textPerfil;
                break;
            default:
                return;
        }

        // Cambiar color del icono a blanco (sobre fondo azul)
        icon.setColorFilter(ContextCompat.getColor(this, R.color.nav_active_text));

        // Cambiar color del texto a blanco
        text.setTextColor(ContextCompat.getColor(this, R.color.nav_active_text));

        // Cambiar fondo del botón
        navItem.setBackgroundResource(R.drawable.nav_button_active_background);

        // Expandir el botón para mostrar el texto completo
        expandButton(navItem, text);
    }

    private void deactivateNavItem(int position) {
        LinearLayout navItem;
        ImageView icon;
        TextView text;

        switch (position) {
            case 0:
                navItem = navHome;
                icon = iconHome;
                text = textHome;
                break;
            case 1:
                navItem = navFavoritos;
                icon = iconFavoritos;
                text = textFavoritos;
                break;
            case 2:
                navItem = navAmigos;
                icon = iconAmigos;
                text = textAmigos;
                break;
            case 3:
                navItem = navPerfil;
                icon = iconPerfil;
                text = textPerfil;
                break;
            default:
                return;
        }

        // Cambiar color del icono
        icon.setColorFilter(ContextCompat.getColor(this, R.color.nav_inactive));

        // Cambiar fondo del botón
        navItem.setBackgroundResource(R.drawable.nav_button_background);

        // Colapsar el botón para ocultar el texto
        collapseButton(navItem, text);
    }

    private void expandButton(LinearLayout button, TextView textView) {
        // Hacer visible el texto
        textView.setVisibility(View.VISIBLE);
        textView.setAlpha(0f);
        
        // Medir el ancho necesario para el texto
        textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int textWidth = textView.getMeasuredWidth();
        
        // Ancho mínimo del botón (solo icono)
        int minWidth = (int) (56 * getResources().getDisplayMetrics().density);
        // Ancho máximo del botón (icono + texto + padding)
        int maxWidth = minWidth + textWidth + (int) (16 * getResources().getDisplayMetrics().density);
        
        // Obtener el ancho actual del botón
        int currentWidth = button.getWidth();
        if (currentWidth == 0) {
            button.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            currentWidth = button.getMeasuredWidth();
        }
        
        // Animación del ancho del botón
        ValueAnimator widthAnimator = ValueAnimator.ofInt(currentWidth, maxWidth);
        widthAnimator.setDuration(450);
        widthAnimator.setInterpolator(new android.view.animation.OvershootInterpolator(0.3f));
        widthAnimator.addUpdateListener(animation -> {
            int width = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = button.getLayoutParams();
            params.width = width;
            button.setLayoutParams(params);
        });
        widthAnimator.start();
        
        // Animación de opacidad del texto
        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0f, 1f);
        alphaAnimator.setDuration(450);
        alphaAnimator.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
        alphaAnimator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            textView.setAlpha(alpha);
        });
        alphaAnimator.start();
    }

    private void collapseButton(LinearLayout button, TextView textView) {
        // Ancho mínimo del botón (solo icono)
        int minWidth = (int) (56 * getResources().getDisplayMetrics().density);
        
        // Obtener el ancho actual del botón
        int currentWidth = button.getWidth();
        if (currentWidth == 0) {
            button.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            currentWidth = button.getMeasuredWidth();
        }
        
        // Animación de opacidad del texto primero
        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(1f, 0f);
        alphaAnimator.setDuration(300);
        alphaAnimator.setInterpolator(new android.view.animation.AccelerateInterpolator());
        alphaAnimator.addUpdateListener(animation -> {
            float alpha = (float) animation.getAnimatedValue();
            textView.setAlpha(alpha);
        });
        alphaAnimator.start();
        
        // Animación del ancho del botón
        ValueAnimator widthAnimator = ValueAnimator.ofInt(currentWidth, minWidth);
        widthAnimator.setDuration(400);
        widthAnimator.setInterpolator(new android.view.animation.AccelerateDecelerateInterpolator());
        widthAnimator.addUpdateListener(animation -> {
            int width = (int) animation.getAnimatedValue();
            ViewGroup.LayoutParams params = button.getLayoutParams();
            params.width = width;
            button.setLayoutParams(params);
        });
        widthAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                textView.setVisibility(View.GONE);
            }
        });
        widthAnimator.start();
    }

    private void loadFragment(Fragment fragment) {
        if (currentFragment == null || !currentFragment.getClass().equals(fragment.getClass())) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            currentFragment = fragment;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (item.getItemId() == R.id.action_tu_crew) {
            startActivity(new android.content.Intent(this, TuCrewActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}