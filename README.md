# firestore-recyclerview-adapter
This sample app uses firestore recycler view adapter to list the items in RecyclerView widget. Data is fetched from firestore database. Make sure you register your app in firebase and add google_services.json file inside app directory.

**References**

Firestore - https://firebase.google.com/docs/firestore  
Firestore RecyclerView Adapter - https://github.com/firebase/FirebaseUI-Android/blob/master/firestore/README.md

**Code snippets** 

**MainActivity.kt** 

```kotlin
class MainActivity : AppCompatActivity() {

    private lateinit var fruitsAdapter: FruitListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val query = FirebaseFirestore.getInstance()
            .collection("Fruits")
            .orderBy("name")
        val options = FirestoreRecyclerOptions.Builder<Fruit>()
            .setQuery(query, Fruit::class.java)
            .build()
        fruitsAdapter = FruitListAdapter(options)

        rvList.layoutManager = LinearLayoutManager(this)
        rvList.adapter = fruitsAdapter
    }

    override fun onStart() {
        super.onStart()
        fruitsAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        fruitsAdapter.stopListening()
    }
}
```

**Fruit.kt**

```kotlin
class Fruit {
    val name: String? = ""
    val imgURL: String? = ""
}
```

**FruitListAdapter.kt**

```kotlin
class FruitListAdapter(options: FirestoreRecyclerOptions<Fruit>) :
    FirestoreRecyclerAdapter<Fruit, FruitItemVH>(
        options
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitItemVH {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fruit_list_item, parent, false)
        return FruitItemVH(view)
    }

    override fun onBindViewHolder(holder: FruitItemVH, position: Int, model: Fruit) {
        holder.bindFruit(model)
    }

    override fun onError(e: FirebaseFirestoreException) {
        Log.e("Adapter", e.message!!)
    }

    class FruitItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindFruit(fruit: Fruit) {
            itemView.tvName.text = fruit.name
            Glide.with(itemView.context).load(fruit.imgURL).into(itemView.ivImage);
        }
    }
}
```

**activity_main.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_margin="4dp"
    tools:context=".ui.MainActivity">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvList"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

**fruit_list_item**

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_marginTop="4dp"
    android:layout_marginBottom="4dp"
    app:cardBackgroundColor="@android:color/white"
    app:cardCornerRadius="10dp"
    app:cardElevation="5dp">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        <ImageView
            android:id="@+id/ivImage"
            android:layout_width="match_parent"
            android:layout_height="150dp"
            android:scaleType="centerCrop"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent"
            tools:src="@mipmap/ic_launcher" />

        <TextView
            android:id="@+id/tvName"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:padding="10dp"
            android:textSize="17sp"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/ivImage"
            tools:text="Android" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</androidx.cardview.widget.CardView>
```
