
# Rapport

**Skriv din rapport här!**

_Du kan ta bort all text som finns sedan tidigare_.

## Följande grundsyn gäller dugga-svar:

- Ett kortfattat svar är att föredra. Svar som är längre än en sida text (skärmdumpar och programkod exkluderat) är onödigt långt.
- Svaret skall ha minst en snutt programkod.
- Svaret skall inkludera en kort övergripande förklarande text som redogör för vad respektive snutt programkod gör eller som svarar på annan teorifråga.
- Svaret skall ha minst en skärmdump. Skärmdumpar skall illustrera exekvering av relevant programkod. Eventuell text i skärmdumpar måste vara läsbar.
- I de fall detta efterfrågas, dela upp delar av ditt svar i för- och nackdelar. Dina för- respektive nackdelar skall vara i form av punktlistor med kortare stycken (3-4 meningar).

Programkod ska se ut som exemplet nedan. Koden måste vara korrekt indenterad då den blir lättare att läsa vilket gör det lättare att hitta syntaktiska fel.

```
function errorCallback(error) {
    switch(error.code) {
        case error.PERMISSION_DENIED:
            // Geolocation API stöds inte, gör något
            break;
        case error.POSITION_UNAVAILABLE:
            // Misslyckat positionsanrop, gör något
            break;
        case error.UNKNOWN_ERROR:
            // Okänt fel, gör något
            break;
    }
}
```

Bilder läggs i samma mapp som markdown-filen.

![](android.png)

Läs gärna:

- Boulos, M.N.K., Warren, J., Gong, J. & Yue, P. (2010) Web GIS in practice VIII: HTML5 and the canvas element for interactive online mapping. International journal of health geographics 9, 14. Shin, Y. &
- Wunsche, B.C. (2013) A smartphone-based golf simulation exercise game for supporting arthritis patients. 2013 28th International Conference of Image and Vision Computing New Zealand (IVCNZ), IEEE, pp. 459–464.
- Wohlin, C., Runeson, P., Höst, M., Ohlsson, M.C., Regnell, B., Wesslén, A. (2012) Experimentation in Software Engineering, Berlin, Heidelberg: Springer Berlin Heidelberg.

First of all I created a listview for the future mountains to be listed on.
```
   <ListView
        android:id="@+id/list"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"></ListView>
 ```
 Then I created a arraylist for the Mountains to exist on and an adapter which purpose is to work as an interface and cooperate between the listview and the arraylist so that the data can be taken and transformed and viewed by the user easily.
```
    ArrayList<Mountain> mountainsList;
    ArrayAdapter<Mountain> adapter;
    [...]
    mountainsList = new ArrayList<>();
    adapter = new ArrayAdapter<>(this, R.layout.list_item_textview, mountainsList);

    ListView listView = findViewById(R.id.list);
    listView.setAdapter(adapter);

```
But in order for the previous steps to work and have something show up on the screen we need to create the actual Mountain object, in doing so I created the Mountain class.
```
public class Mountain {
    private String ID;
    private String size;
    private String name;
    private String location;
    public Mountain (String ID, String name, String location, String size) {
        ID = ID;
        this.name = name;
        this.location = location;
        this.size = size;
    }
    public String getID(){
        return ID;
    }
    public String getSize(){
        return size;
    }
    public String getName(){
        return name;
    }
    public String getLocation(){
        return location;
    }
    @Override
    public String toString(){
        return name;
    }
}
```
The momuntain class contains of gets for each variable so that I can for example get the name and loaction for the specific mountain on the popup message depending on which mountain I click on in the list.

```
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                String message = "Berget " + mountainsList.get(i).getName() + " finns i " + mountainsList.get(i).getLocation() +
                        " och är " + mountainsList.get(i).getSize() + " stort.";
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
```
Above is the example of how i get from a specific index in the list.
Before the data can actually get passed there we need to add the JSON class given by the instructors.
The point of that class is to return a JSON object.

```
    @SuppressLint("StaticFieldLeak")
    private class JsonTask extends AsyncTask<String, String, String> {

        private HttpURLConnection connection = null;
        private BufferedReader reader = null;

        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuilder builder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null && !isCancelled()) {
                    builder.append(line).append("\n");
                }
                return builder.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
```
Lastly a method to parese the data is added. This is where the mountain object gets created and split into Strings and ints if I had int values.
```
        @Override
        protected void onPostExecute(String json) {
            try {
                mountainsList.clear();
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String ID = jsonObject.getString("ID");
                    String name = jsonObject.getString("name");
                    String location = jsonObject.getString("location");
                    String size = jsonObject.getString("size");
                    Mountain mountain = new Mountain(ID, name, location, size);
                    mountainsList.add(mountain);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Log.e("tag","E:"+e.getMessage());
            }
        }
```
And finally we have something looking liek this with a Toast message on click.
![screenshot](https://user-images.githubusercontent.com/62877630/130123105-406b3e12-a571-4587-bdb1-7b22bd6756b8.png)
