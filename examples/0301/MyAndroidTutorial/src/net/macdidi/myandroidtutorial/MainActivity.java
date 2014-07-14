package net.macdidi.myandroidtutorial;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	
	private ListView item_list;	
	private TextView show_app_name;
	
	// ListView使用的自定Adapter物件
	private ItemAdapter itemAdapter;
	// 儲存所有記事本的List物件
	private List<Item> items;
	
	// 選單項目物件
	private MenuItem add_item, search_item, revert_item, share_item, delete_item;
	
	// 已選擇項目數量
	private int selectedCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		processViews();
		processControllers();
				
		// 加入範例資料		
		items = new ArrayList<Item>();
        
        items.add(new Item(1, new Date().getTime(), Colors.RED, "關於Android Tutorial的事情.", "Hello content", "", 0, 0, 0));
        items.add(new Item(2, new Date().getTime(), Colors.BLUE, "一隻非常可愛的小狗狗!", "她的名字叫「大熱狗」，又叫\n作「奶嘴」，是一隻非常可愛\n的小狗。", "", 0, 0, 0));
        items.add(new Item(3, new Date().getTime(), Colors.GREEN, "一首非常好聽的音樂！", "Hello content", "", 0, 0, 0));
        
        // 建立自定Adapter物件
        itemAdapter = new ItemAdapter(this, R.layout.single_item, items);
        item_list.setAdapter(itemAdapter);        
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 如果被啟動的Activity元件傳回確定的結果
		if (resultCode == Activity.RESULT_OK) {
			// 讀取記事物件
			Item item = (Item) data.getExtras().getSerializable(
					"net.macdidi.myandroidtutorial.Item");
			
			// 如果是新增記事
			if (requestCode == 0) {
				// 設定記事物件的編號與日期時間
				item.setId(items.size() + 1);
				
				// 加入新增的記事物件
				items.add(item);
				
				// 通知資料改變
				itemAdapter.notifyDataSetChanged();
			}
			// 如果是修改記事
			else if (requestCode == 1) {
				// 讀取記事編號
				int position = data.getIntExtra("position", -1);
				
				if (position != -1) {
					// 設定修改的記事物件
					items.set(position, item);
					itemAdapter.notifyDataSetChanged();
				}
			}
		}
	}

	private void processViews() {
    	item_list = (ListView)findViewById(R.id.item_list);
    	show_app_name = (TextView) findViewById(R.id.show_app_name);
    }

    private void processControllers() {
    	
		// 建立選單項目點擊監聽物件
		OnItemClickListener itemListener = new OnItemClickListener() {
		    @Override
		    public void onItemClick(AdapterView<?> parent, View view, 
		            int position, long id) {
                // 讀取選擇的記事物件
                Item item = itemAdapter.getItem(position);
		        
                // 如果已經有勾選的項目
                if (selectedCount > 0) {
                    // 處理是否顯示已選擇項目 
                    processMenu(item);
                    // 重新設定記事項目
                    itemAdapter.set(position, item);
                }
                else {    				
    				Intent intent = new Intent(
    						"net.macdidi.myandroidtutorial.EDIT_ITEM");
    				
    				// 設定記事編號與記事物件
    				intent.putExtra("position", position);
    				intent.putExtra("net.macdidi.myandroidtutorial.Item", item);
    				
    				startActivityForResult(intent, 1);
                }
		    }
		};
		
		// 註冊選單項目點擊監聽物件
		item_list.setOnItemClickListener(itemListener);
		
		// 建立記事項目長按監聽物件
		OnItemLongClickListener itemLongListener = new OnItemLongClickListener() {
		    @Override
		    public boolean onItemLongClick(AdapterView<?> parent, View view, 
		            int position, long id) {
                // 讀取選擇的記事物件
                Item item = itemAdapter.getItem(position);
                // 處理是否顯示已選擇項目
                processMenu(item);
                // 重新設定記事項目
                itemAdapter.set(position, item);
                return true;
		    }
		};
		
		// 註冊選單項目長按監聽物件
		item_list.setOnItemLongClickListener(itemLongListener);
		
		// 建立長按監聽物件
		OnLongClickListener listener = new OnLongClickListener() {

			@Override
			public boolean onLongClick(View view) {
				AlertDialog.Builder dialog = 
						new AlertDialog.Builder(MainActivity.this);
				dialog.setTitle(R.string.app_name)
				      .setMessage(R.string.about)
				      .show();
				return false;
			}
			
		};
		
		// 註冊長按監聽物件
		show_app_name.setOnLongClickListener(listener);	
    }	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater menuInflater = getMenuInflater();
	    menuInflater.inflate(R.menu.main_menu, menu);
	    
	    // 取得選單項目物件
	    add_item = menu.findItem(R.id.add_item);
        search_item = menu.findItem(R.id.search_item);
        revert_item = menu.findItem(R.id.revert_item);
        share_item = menu.findItem(R.id.share_item);
        delete_item = menu.findItem(R.id.delete_item);
	    
        // 設定選單項目
        processMenu(null);
        
	    return true;
	}
	
	public void clickMenuItem(MenuItem item) {
		// 使用參數取得使用者選擇的選單項目元件編號
		int itemId = item.getItemId();
		
		switch (itemId) {
		case R.id.search_item:
			break;
		case R.id.add_item:
			Intent intent = new Intent("net.macdidi.myandroidtutorial.ADD_ITEM");
			startActivityForResult(intent, 0);
			break;
		// 取消所有已勾選的項目
		case R.id.revert_item:
            for (int i = 0; i < itemAdapter.getCount(); i++) {
                Item ri = itemAdapter.getItem(i);
                
                if (ri.isSelected()) {
                    ri.setSelected(false);
                    itemAdapter.set(i, ri);
                }
            }
            
            selectedCount = 0;
            processMenu(null);          
		    
			break;
		// 刪除
		case R.id.delete_item:
		    // 沒有選擇
		    if (selectedCount == 0) {
		        break;
		    }
		    
            // 建立與顯示詢問是否刪除的對話框
            AlertDialog.Builder d = new AlertDialog.Builder(this);
            String message = getString(R.string.delete_item);
            d.setTitle(R.string.delete)
             .setMessage(String.format(message, selectedCount));
            d.setPositiveButton(android.R.string.yes, 
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 刪除所有已勾選的項目
                            for (int i = 0; i < itemAdapter.getCount(); i++) {
                                Item item = itemAdapter.get(i);
                                
                                // 如果是勾選的項目就刪除它
                                if (item.isSelected()) {
                                    itemAdapter.remove(item);
                                }
                            }
                            
                            // 通知資料改變
                            itemAdapter.notifyDataSetChanged();
                        }
                    });
            d.setNegativeButton(android.R.string.no, null);
            d.show();
		    
			break;
		case R.id.googleplus_item:
			break;
		case R.id.facebook_item:
			break;
		}		
	}
	
	// 處理是否顯示已選擇項目
	private void processMenu(Item item) {
		// 如果需要設定記事項目
    	if (item != null) {
    		// 設定已勾選的狀態
	    	item.setSelected(!item.isSelected());
			
	    	// 計算已勾選數量
			if (item.isSelected()) {
				selectedCount++;
			}
			else {
				selectedCount--;
			}
    	}
		
    	// 根據選擇的狀況，設定是否顯示選單項目
    	add_item.setVisible(selectedCount == 0);
		search_item.setVisible(selectedCount == 0);
		revert_item.setVisible(selectedCount > 0);
		share_item.setVisible(selectedCount > 0);
		delete_item.setVisible(selectedCount > 0);
	}
	
	public void aboutApp(View view) {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
}