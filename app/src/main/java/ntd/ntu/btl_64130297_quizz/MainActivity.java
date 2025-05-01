package ntd.ntu.btl_64130297_quizz;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomnav;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomnav=findViewById(R.id.bot_nav);
        //lang nghe
        bottomnav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment =null;
                int itemId =item.getItemId();
                if(itemId == R.id.mnu_home){
                    selectedFragment =new HomeFragment();
                }else if(itemId == R.id.mnu_video){
                    selectedFragment =new VideoFragment();
                }
                if(selectedFragment !=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_main,selectedFragment).commit();
                }
                return true;
            }
        });

    }
}