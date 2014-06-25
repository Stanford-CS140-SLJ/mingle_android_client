package com.example.mingle;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;
import com.fortysevendeg.swipelistview.SwipeListView.OnLoadMoreListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.String;
import java.util.ArrayList;
import java.util.List;


public class HuntActivity extends ActionBarActivity  {
	private SwipeListView allchatlistview;
	private ListView currentlychattinglistview;
	private ItemAdapter adapter;
	private List<ItemRow> itemData;
	
	
	private ArrayList<ItemRow> currentItemData;
	 
	  private ArrayAdapter<ItemRow> currentlistAdapter ;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hunt);
        
        new LoadDataTask(this.getApplication(), 10).execute();
        //MingleUser currUser = ((MingleApplication) this.getApplication()).currUser;
        //((MingleApplication) this.getApplication()).initHelper.requestUserList(currUser.getUid(), currUser.getSex(), 
        //																				currUser.getLat(), currUser.getLong(), currUser.getDist(), 10);

        ((MingleApplication) this.getApplication()).initHelper.changeContext(this);

        Context context = getApplicationContext();
        CharSequence text = "User created successfully!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

        allchatlistview=(SwipeListView)findViewById(R.id.All);
        currentlychattinglistview=(ListView) findViewById(R.id.mingling);
        itemData=new ArrayList<ItemRow>();
        adapter=new ItemAdapter(this,R.layout.custom_row,itemData);
        
        currentItemData = new ArrayList<ItemRow>();
        
        currentlistAdapter = new ItemAdapter(this, R.layout.custom_row, currentItemData);
        
        
        // Add more planets. If you passed a String[] instead of a List<String>   
        // into the ArrayAdapter constructor, you must not add more items.   
        // Otherwise an exception will occur.  
         
          
        // Set the ArrayAdapter as the ListView's adapter.  
        currentlychattinglistview.setAdapter( currentlistAdapter );        
        
        
        final Activity curActivity = this;
        
        allchatlistview.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }
    
            @Override
            public void onClosed(int position, boolean fromRight) {
            }
    
            @Override
            public void onListChanged() {
            }
    
            @Override
            public void onMove(int position, float x) {
            }
    
            @Override
            public void onStartOpen(int position, int action, boolean right) {
                Log.d("swipe", String.format("onStartOpen %d - action %d", position, action));
                Intent i = new Intent(curActivity, ChatroomActivity.class);
                curActivity.startActivity(i);
                allchatlistview.openAnimate(position); //when you touch front view it will open
                currentlistAdapter.add(itemData.get(position));
            }
    
            @Override
            public void onStartClose(int position, boolean right) {
                Log.d("swipe", String.format("onStartClose %d", position));
            }
    
            @Override
            public void onClickFrontView(int position) {
                Log.d("swipe", String.format("onClickFrontView %d", position));

            }
    
            @Override
            public void onClickBackView(int position) {
                Log.d("swipe", String.format("onClickBackView %d", position));
    
                allchatlistview.closeAnimate(position);//when you touch back view it will close
            }
    
            @Override
            public void onDismiss(int[] reverseSortedPositions) {
    
            }
    
        });
        
        //These are the swipe listview settings. you can change these
        //setting as your requirement
        allchatlistview.setSwipeMode(SwipeListView.SWIPE_MODE_LEFT); // there are five swiping modes
        allchatlistview.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL); //there are four swipe actions
        allchatlistview.setSwipeActionRight(SwipeListView.SWIPE_ACTION_REVEAL);
        allchatlistview.setOffsetLeft(convertDpToPixel(0f)); // left side offset
        allchatlistview.setOffsetRight(convertDpToPixel(0f)); // right side offset
        allchatlistview.setAnimationTime(50); // animation time
        allchatlistview.setSwipeOpenOnLongPress(true); // enable or disable SwipeOpenOnLongPress
        
        allchatlistview.setAdapter(adapter);
       final ApplicationWrapper wrapper = new ApplicationWrapper(this.getApplication());
        allchatlistview.setOnLoadMoreListener(new OnLoadMoreListener() {
            public void onLoadMore() {
                // Do the work to load more items at the end of list
                // here
            	System.out.println("loadmore start");
            	new LoadDataTask(wrapper.curApp, 5).execute();
            	
            	System.out.println("On load more called!!");
            	
            	 //Context context = getApplicationContext();
                 //CharSequence text = "Data End!";
                 //int duration = Toast.LENGTH_SHORT;

                 //Toast toast = Toast.makeText(context, text, duration);
                 //toast.show();
            }
        });
         
    }
    
	private class ApplicationWrapper {
		Application curApp;
		public ApplicationWrapper(Application app) {
			curApp = app;
		}
	}
	
    public int convertDpToPixel(float dp) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }
    
    /* Method name: showList
     * Retrieve data from HttpHelper. Content of data is the list of nearby users of opposite sex.
     * This method also appends the new data to the list and shows them on the screen.
     */
    public void updateList(JSONArray listData){
    	System.out.println("showlist!");
    	System.out.println(listData.toString());
        for(int i = 0 ; i < listData.length(); i++) {
            try {
                JSONObject shownUser = listData.getJSONObject(i);
                ((MingleApplication) this.getApplication()).currUser.addUser(shownUser);
               // Bitmap drawable = BitmapFactory.decodeFile(shownUser.getString("PHOTO1"));
                /*getResources().getDrawable(R.drawable.ic_launcher))*/
                //byte[] imageAsBytes = Base64.decode(myImageData.getBytes());
                //Bitmap bp = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                //BitmapDrawable x = BitmapDrawable(getResources().getDrawable(R.drawable.ic_launcher), drawable);
                
                itemData.add(new ItemRow(shownUser.getString("COMM") + " " + shownUser.getString("NUM"), (Drawable) getResources().getDrawable(R.drawable.ic_launcher) ));
            } catch (JSONException e){
                e.printStackTrace();
            }
        }        
    }
    
    private class LoadDataTask extends AsyncTask<Void, Void, Void> {
    	
    	private Application curApp;
    	
    	private int load_num;
    	
    	public LoadDataTask(Application app, int load_num) {
    		curApp = app;
    		this.load_num = load_num;
    	}
    	
		@Override
		protected Void doInBackground(Void... params) {

			if (isCancelled()) {
				return null;
			}

	        
			MingleUser currUser = ((MingleApplication) curApp).currUser;
        	((MingleApplication) curApp).initHelper.requestUserList(currUser.getUid(), currUser.getSex(), 
					currUser.getLat(), currUser.getLong(), currUser.getDist(), load_num);

			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			// We need notify the adapter that the data have been changed
			adapter.notifyDataSetChanged();

			// Call onLoadMoreComplete when the LoadMore task, has finished
			allchatlistview.onLoadMoreComplete();

			super.onPostExecute(result);
		}

		@Override
		protected void onCancelled() {
			// Notify the loading more operation has finished
			allchatlistview.onLoadMoreComplete();
		}
	}
}


