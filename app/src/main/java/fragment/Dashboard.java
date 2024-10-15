package fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mybimo.R;

import java.util.ArrayList;

import carousel.Materi;
import carousel.MateriAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Dashboard#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Dashboard extends Fragment {

    private RecyclerView recyclerView;
    private MateriAdapter materiAdapter;

    public Dashboard() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dashboard.
     */
    // TODO: Rename and change types and number of parameters
    public static Dashboard newInstance(String param1, String param2) {
        Dashboard fragment = new Dashboard();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);

        ArrayList<Materi> materiArrayList = new ArrayList<>();
        materiArrayList.add(new Materi(R.drawable.icon_vocab, "Vocabulary"));
        materiArrayList.add(new Materi(R.drawable.icon_grammer, "Grammerly"));
        materiArrayList.add(new Materi(R.drawable.icon_listening, "Listening"));
        materiArrayList.add(new Materi(R.drawable.icon_pronount, "Pronount"));
        materiArrayList.add(new Materi(R.drawable.icon_listening, "Listening"));

        materiAdapter = new MateriAdapter(getActivity(), materiArrayList);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        recyclerView.setAdapter(materiAdapter);

        return view;
    }
}