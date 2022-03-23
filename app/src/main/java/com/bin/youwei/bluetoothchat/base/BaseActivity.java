package com.bin.youwei.bluetoothchat.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by BinYouWei on 2022/1/2
 */
public abstract class BaseActivity<V extends ViewBinding> extends AppCompatActivity {
    public V binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            // 获得泛型中的实际类型，可能会存在多个类型
            Class<V> clazz = (Class<V>) ((ParameterizedType) type).getActualTypeArguments()[0];
            try {
                // 反射inflate方法
                Method inflate = clazz.getMethod("inflate", LayoutInflater.class);
                binding = (V)inflate.invoke(null, getLayoutInflater());
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            setContentView(binding.getRoot());
        }
        initView(binding.getRoot());
        setListener();
    }

    /**
     * 界面初始化
     */
    public abstract void initView(View view);

    /**
     * 设置监听事件
     */
    public abstract void setListener();
}
