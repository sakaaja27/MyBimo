    package fragment;

    import static auth.DB_Contract.ip;

    import android.os.Bundle;

    import androidx.fragment.app.Fragment;
    import androidx.recyclerview.widget.LinearLayoutManager;
    import androidx.recyclerview.widget.RecyclerView;

    import android.util.Log;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.RelativeLayout;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.android.volley.Request;
    import com.android.volley.Response;
    import com.android.volley.VolleyError;
    import com.android.volley.toolbox.JsonArrayRequest;
    import com.android.volley.toolbox.Volley;
    import com.example.mybimo.Main;
    import com.example.mybimo.R;

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.ArrayList;

    import Adapter.AdapterHistory;
    import Adapter.GetHistory;
    import Adapter.HasilQuiz;

    public class MyCourse extends Fragment {

        private View view;
        private static final String TAG = "MyCourse";

        public MyCourse() {
            // Required empty public constructor
        }

        public static MyCourse newInstance(String param1, String param2) {
            MyCourse fragment = new MyCourse();
            Bundle args = new Bundle();
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
            view = inflater.inflate(R.layout.fragment_course, container, false);




            return view;
        }


    }
