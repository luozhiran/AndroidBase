package com.plugin.mvvm.common;

import android.content.Context;
import android.content.res.Resources;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import androidx.databinding.ViewDataBinding;

public class ErrorUtils {

    static void throwMissingVariable(ViewDataBinding binding, int bindingVariable, int layoutRes) {
        Context context = binding.getRoot().getContext();
        Resources resources = context.getResources();
        String layoutName = resources.getResourceName(layoutRes);

        String bindingVariableName;
        try {
            bindingVariableName = getBindingVariableName(context, bindingVariable);
        } catch (Resources.NotFoundException var8) {
            bindingVariableName = "" + bindingVariable;
        }

        throw new IllegalStateException("Could not bind variable '" + bindingVariableName + "' in layout '" + layoutName + "'");
    }


    static String getBindingVariableName(Context context, int bindingVariable) throws Resources.NotFoundException {
        try {
            return getBindingVariableByDataBinderMapper(bindingVariable);
        } catch (Exception var5) {
            try {
                return getBindingVariableByBR(context, bindingVariable);
            } catch (Exception var4) {
                throw new Resources.NotFoundException("" + bindingVariable);
            }
        }
    }


    private static String getBindingVariableByDataBinderMapper(int bindingVariable) throws Exception {
        Class<?> dataBinderMapper = Class.forName("androidx.databinding.DataBinderMapper");
        Method convertIdMethod = dataBinderMapper.getDeclaredMethod("convertBrIdToString", Integer.TYPE);
        convertIdMethod.setAccessible(true);
        Constructor constructor = dataBinderMapper.getDeclaredConstructor();
        constructor.setAccessible(true);
        Object instance = constructor.newInstance();
        Object result = convertIdMethod.invoke(instance, bindingVariable);
        return (String)result;
    }


    private static String getBindingVariableByBR(Context context, int bindingVariable) throws Exception {
        String packageName = context.getPackageName();
        Class BRClass = Class.forName(packageName + ".BR");
        Field[] fields = BRClass.getFields();
        Field[] var5 = fields;
        int var6 = fields.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Field field = var5[var7];
            int value = field.getInt((Object)null);
            if (value == bindingVariable) {
                return field.getName();
            }
        }

        throw new Exception("not found");
    }
}
