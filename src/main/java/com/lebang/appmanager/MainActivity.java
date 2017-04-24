package com.lebang.appmanager;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ManagerAdapter.OnItemListener, AdapterView.OnItemClickListener {

    private RecyclerView mRecycylerView;
    private LinearLayoutManager mLayoutManager;
    private ManagerAdapter mAdapter;
    private ArrayList<AppBean> mAppList = new ArrayList<>();

    private ListView mMarkListView;
    private ArrayList<String> mMarkList = new ArrayList<>();
    private ArrayAdapter mMarkAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        initData();
        mRecycylerView = (RecyclerView) findViewById(R.id.recycler_activity_main);
        mAdapter = new ManagerAdapter(this, mAppList);
        mAdapter.setOnItemClickListener(this);
        mLayoutManager = new LinearLayoutManager(this);
        mRecycylerView.setLayoutManager(mLayoutManager);
        mRecycylerView.setAdapter(mAdapter);

        mMarkListView = (ListView) findViewById(R.id.list_activity_main);
        mMarkAdapter = new ArrayAdapter(this, R.layout.item_mark, mMarkList);
        mMarkListView.setAdapter(mMarkAdapter);
        mMarkListView.setOnItemClickListener(this);
    }

    private void initData() {
        PackageManager mPackageManager = this.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = mPackageManager.queryIntentActivities(intent, 0);
        Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(mPackageManager));
        if (mAppList != null) {
            mAppList.clear();
            for (ResolveInfo reInfo : resolveInfos) {
                String activityName = reInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
                String pkgName = reInfo.activityInfo.packageName; // 获得应用程序的包名
                String appLabel = (String) reInfo.loadLabel(mPackageManager); // 获得应用程序的应用名
                Drawable icon = reInfo.loadIcon(mPackageManager); // 获得应用程序图标
                // 为应用程序的启动Activity 准备Intent
                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(pkgName, activityName));
                // 创建一个AppInfo对象，并赋值
                AppBean appInfo = new AppBean();
                appInfo.label_name = appLabel;
                appInfo.package_name = pkgName;
                appInfo.img = icon;
                appInfo.open_intent = launchIntent;
                //卸载App的Intent
                Uri packageURI = Uri.parse("package:" + pkgName);
                appInfo.uninstall_intent = new Intent(Intent.ACTION_DELETE, packageURI);

                mAppList.add(appInfo); // 添加至列表中
                if (!mMarkList.contains(appLabel.substring(0, 1))) {
                    mMarkList.add(appLabel.substring(0, 1));//添加索引至书签列表中
                }
            }
        }
    }

    @Override
    public void onClick(int pos) {
        Intent intent = mAppList.get(pos).open_intent;
        startActivity(intent);
    }

    @Override
    public void onUninstallClick(int pos) {
        Intent intent = mAppList.get(pos).uninstall_intent;
        //刷新界面
        mAppList.remove(pos);
        mMarkList.remove(pos);
        startActivity(intent);
        mAdapter.notifyDataSetChanged();
        mMarkAdapter.notifyDataSetChanged();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Toast.makeText(this, "您选择了" + mMarkList.get(position), Toast.LENGTH_SHORT).show();
//        mRecycylerView.scrollToPosition(11);
//        mRecycylerView.smoothScrollToPosition(15);
        for (int i = 0; i < mAppList.size(); i++) {
            if (mAppList.get(i).label_name.contains(mMarkList.get(position))) {
                mLayoutManager.scrollToPositionWithOffset(i, 0);
                break;
            }
        }
    }
}