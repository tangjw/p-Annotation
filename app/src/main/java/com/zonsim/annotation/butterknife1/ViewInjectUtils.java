package com.zonsim.annotation.butterknife1;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * desc
 * <p>
 * Created by tangjunwei on 2018/6/26.
 * <a href="mailto:tjwabc@gmail.com">Contact me</a>
 * <a href="https://github.com/tangjw">Follow me</a>
 */
public class ViewInjectUtils {
    public static void inject(Activity activity) {
        injectContentView(activity);
        injectView(activity);
        injectEvent1(activity);
    }
    
    private static void injectEvent(final Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        
        for (final Method method : methods) {
            OnClick onClick = method.getAnnotation(OnClick.class);
            
            if (onClick == null) continue;
            
            int[] viewId = onClick.value();
            method.setAccessible(true);
    
            Object listener = Proxy.newProxyInstance(View.OnClickListener.class.getClassLoader(),
                    new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            return method.invoke(activity, args);
                        }
                    });
    
            for (int id : viewId) {
                View view = activity.findViewById(id);
                try {
                    Method setOnClickListener = view.getClass().getMethod("setOnClickListener", View.OnClickListener.class);
                    setOnClickListener.invoke(view, listener);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
    
        }
        
    }
    
    private static void bindClicks(final Activity activity) {
        Class<? extends Activity> aClass = activity.getClass();
        Method[] declaredMethods = aClass.getDeclaredMethods();//反射获取方法
        //遍历方法，判断方法上是否有@OnClick注解
        for (final Method method : declaredMethods) {
            if(method.isAnnotationPresent(OnClick.class)){
                OnClick annotation = method.getAnnotation(OnClick.class);
                int[] viewIds = annotation.value();//拿到该方法上注解的view的id集
                for (int viewId : viewIds) {
                    final View view = activity.findViewById(viewId);
                    if(null != view){
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    method.setAccessible(true);//破封装
                                    method.invoke(activity,view);//调起该带有@OnClick注解方法
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                }
            }
        }
    }
    
    
    private static void injectEvent1(final Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (final Method method2 : methods) {
            OnClick click = method2.getAnnotation(OnClick.class);
            if (click != null) {
                
                int[] viewId = click.value();
                method2.setAccessible(true);
                Object listener = Proxy.newProxyInstance(View.OnClickListener.class.getClassLoader(),
                        new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                return method2.invoke(activity, args);
                            }
                        });
                
                try {
                    for (int id : viewId) {
                        View v = activity.findViewById(id);
                        Method setClickListener = v.getClass().getMethod("setOnClickListener", View.OnClickListener.class);
                        try {
                            setClickListener.invoke(v, listener);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    private static void injectView(Activity activity) {
        Class<? extends Activity> clazz = activity.getClass();
        
        Field[] fields = clazz.getDeclaredFields();
        
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            
            if (viewInject == null) continue;
            
            int viewId = viewInject.value();
            
            View view = activity.findViewById(viewId);
            
            try {
                field.setAccessible(true);
                field.set(activity, view);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            
        }
        
    }
    
    private static void injectContentView(Activity activity) {
        
        
        Class<? extends Activity> clazz = activity.getClass();
        
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        
        if (contentView == null) return;
        
        int layoutResId = contentView.value();
        
        try {
            Method setContentViewMethod = clazz.getMethod("setContentView", int.class);
            setContentViewMethod.invoke(activity, layoutResId);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        
    }
}
