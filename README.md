# firestore-recyclerview-adapter
This sample app uses the below firestore recycler adapters to list the items in RecyclerView widget

* **FirestoreRecyclerAdapter**
  * Listens to live changes under a collection(document insert/update/delete in a collection)
  * Does not support pagination
  
* **FirestorePagingAdapter**
  * Supports pagination
  * Does not listen to live changes under a collection(document insert/update/delete in a collection)

Data is fetched from firestore database. Make sure you register your app in firebase and add google_services.json file inside app directory.

**References**

Firestore - https://firebase.google.com/docs/firestore  
Firestore RecyclerView Adapter - https://github.com/firebase/FirebaseUI-Android/blob/master/firestore/README.md



![FirestoreRecyclerAdapters](https://user-images.githubusercontent.com/40466166/95305779-12eb4780-08a4-11eb-964b-2069c9fe2997.png)

**Code snippets** 

**MainActivity.kt** 

```kotlin
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pager.adapter = PagerAdapter(2, this)
        TabLayoutMediator(tabLayout, pager) { tab, position ->
            tab.text = when (position) {
                0 -> "Without Paging"
                1 -> "With Paging"
                else -> ""
            }
        }.attach()
    }
}
```

**activity_main.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <com.google.android.material.tabs.TabLayout
        android:id="@+id/tabLayout"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />

    <androidx.viewpager2.widget.ViewPager2
        android:id="@+id/pager"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />

</LinearLayout>
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

**FruitListPagingAdapter**

```kotlin
class FruitListPagingAdapter(options: FirestorePagingOptions<Fruit>) :
    FirestorePagingAdapter<Fruit, FruitListPagingAdapter.FruitItemVH>(options) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FruitItemVH {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.fruit_list_item, parent, false)
        return FruitItemVH(view)
    }

    override fun onBindViewHolder(holder: FruitItemVH, position: Int, model: Fruit) {
        holder.bindFruit(model)
    }

    class FruitItemVH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindFruit(fruit: Fruit) {
            itemView.tvName.text = fruit.name
            Glide.with(itemView.context).load(fruit.imgURL).into(itemView.ivImage);
        }
    }
}
```

**fruit_list_item.xml**

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

**FirestoreRecyclerViewFragment**

```kotlin
class FirestoreRecyclerViewFragment : Fragment() {

    private lateinit var fruitsAdapter: FruitListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = FirebaseFirestore.getInstance()
            .collection("Fruits")
            .orderBy("name")
        val options = FirestoreRecyclerOptions.Builder<Fruit>()
            .setQuery(query, Fruit::class.java)
            .build()
        fruitsAdapter = FruitListAdapter(options)

        rvList.layoutManager = LinearLayoutManager(requireActivity())
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

    companion object {
        @JvmStatic
        fun newInstance() = FirestoreRecyclerViewFragment()
    }
}
```

**FirestoreRecyclerViewPagingFragment**

```kotlin
class FirestoreRecyclerViewPagingFragment : Fragment() {

    private lateinit var fruitsAdapter: FruitListPagingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recycler_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val query = FirebaseFirestore.getInstance()
            .collection("Fruits")
            .orderBy("name", Query.Direction.DESCENDING)
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPrefetchDistance(5)
            .setPageSize(5) // No of items loaded from datasource
            .build()
        val options = FirestorePagingOptions.Builder<Fruit>()
            .setQuery(query, config, Fruit::class.java)
            .build()

        fruitsAdapter = FruitListPagingAdapter(options)

        rvList.layoutManager = LinearLayoutManager(requireActivity())
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

    companion object {
        @JvmStatic
        fun newInstance() = FirestoreRecyclerViewPagingFragment()
    }
}
```

**fragment_recycler_view.xml**

```xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:layout_margin="4dp"
    tools:context=".ui.activities.MainActivity">

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
