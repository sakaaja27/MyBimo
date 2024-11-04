package materi;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mybimo.R;

import java.util.ArrayList;

import Adapter.AdapterSubVocab;
//import carousel.MateriSub;

public class MateriVocabullary extends AppCompatActivity {
    private AdapterSubVocab materiSub;
    private RecyclerView recyclerView;
    private ImageView arrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_materi_vocabullary);

        recyclerView = findViewById(R.id.recycler_view);
        arrow = findViewById(R.id.arrow_back);

//        ArrayList<MateriSub> materiSubArrayList = new ArrayList<>();
//        materiSubArrayList.add(new MateriSub("Vocabullary"));
//        materiSubArrayList.add(new MateriSub("Vocabullary"));
//        materiSubArrayList.add(new MateriSub("Vocabullary"));
//        materiSubArrayList.add(new MateriSub("Vocabullary"));
//        materiSubArrayList.add(new MateriSub("Vocabullary"));
//        materiSubArrayList.add(new MateriSub("Vocabullary"));
//        materiSubArrayList.add(new MateriSub("Vocabullary"));

//        materiSub = new AdapterSubVocab(this, materiSubArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(materiSub);

        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}