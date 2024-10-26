package materi;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybimo.R;

import java.util.ArrayList;

import Adapter.AdapterSubGrammer;
import carousel.MateriSub;

public class MateriGrammerly extends AppCompatActivity {

    private AdapterSubGrammer materisub;
    private RecyclerView recyclerView;
    private ImageView arrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_materi_grammerly);

        recyclerView = findViewById(R.id.recycler_view);
        arrow = findViewById(R.id.arrow_back);

        ArrayList<MateriSub> materiSubArrayList = new ArrayList<>();
        materiSubArrayList.add(new MateriSub("Dasar Vocabulary"));
        materiSubArrayList.add(new MateriSub("Medium Vocabulary"));
        materiSubArrayList.add(new MateriSub("Vocabulary"));
        materiSubArrayList.add(new MateriSub("Vocabulary"));
        materiSubArrayList.add(new MateriSub("Vocabulary"));
        materiSubArrayList.add(new MateriSub("Vocabulary"));
        materiSubArrayList.add(new MateriSub("Vocabulary"));
        materiSubArrayList.add(new MateriSub("Dasar Vocabulary"));
        materiSubArrayList.add(new MateriSub("Medium Vocabulary"));
        materiSubArrayList.add(new MateriSub("Vocabulary"));
        materiSubArrayList.add(new MateriSub("Vocabulary"));
        materiSubArrayList.add(new MateriSub("Vocabulary"));
        materiSubArrayList.add(new MateriSub("Vocabulary"));
        materiSubArrayList.add(new MateriSub("Vocabulary"));

        materisub = new AdapterSubGrammer(this, materiSubArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(materisub);

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Kembali ke aktivitas sebelumnya
            }
        });


    }




}