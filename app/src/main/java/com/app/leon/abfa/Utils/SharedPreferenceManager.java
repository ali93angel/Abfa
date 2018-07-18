package com.app.leon.abfa.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.lang.reflect.Type;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Set;

/**
 * Created by Leon on 12/13/2017.
 */

public class SharedPreferenceManager implements ISharedPreferenceManager {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String intType = "int";
    private String stringType = "string";
    private String floatType = "float";

    public SharedPreferenceManager(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Override
    public boolean CheckIsNotEmpty(String key) {
        if (sharedPreferences == null) {
            return false;
        } else if (sharedPreferences.getString(key, "").length() < 1) {
            return false;
        }
        return true;
    }

    private void put(String key, int value) {
        editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
        editor.commit();
    }

    private void put(String key, String value) {
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
        editor.commit();
    }

    private void put(String key, boolean value) {
        editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
        editor.commit();
    }

    private void put(String key, Set<String> value) {
        editor = sharedPreferences.edit();
        editor.putStringSet(key, value);
        editor.apply();
        editor.commit();
    }

    private void put(String key, float value) {
        editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.apply();
        editor.commit();
    }

    private void put(String key, long value) {
        editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
        editor.commit();
    }

    @Override
    public <T> void put(T key, T value) {
        String keyString = key.toString();
        java.lang.reflect.Type type = value.getClass();
        String stringValue = value.toString();
        String valueTypeString = getType(type);
        put(getKeyTypeString(keyString), valueTypeString);
        if (type == Integer.class) {
            int v = new Integer(value.toString());
            put(keyString, v);
        }
        if (type == String.class) {
            String v = value.toString();
            put(keyString, v);
        }
        if (type == Boolean.class) {
            boolean v = new Boolean(value.toString());
            put(keyString, v);
        }
        if (type == Long.class) {
            long v = new Long(stringValue);
            put(keyString, v);
        }
        if (type == Set.class) {
            Set<String> v = (Set<String>) value;
            put(keyString, v);
        }
        if (type == Float.class) {
            float v = new Float(stringValue);
            put(keyString, v);
        }
    }

    private int getInt(String key) {
        return sharedPreferences.getInt(key, 0);
    }

    private String getString(String key) {
        return sharedPreferences.getString(key, "");
    }

    private boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    private Set<String> getSetString(String key) {
        return sharedPreferences.getStringSet(key, null);
    }

    private float getFloat(String key) {
        return sharedPreferences.getFloat(key, 0);
    }

    private long getLong(String key) {
        return sharedPreferences.getLong(key, 0);
    }

    @Override
    public <T1, T2> T2 get(T1 key) {
        String keyString = key.toString();
        String keyType = getString(getKeyTypeString(keyString));
        if (keyType == "int") {
            int value = getInt(keyString);
            return (T2) (Object) value;
        }
        if (keyType == "string") {
            return (T2) getString(keyString);
        }
        if (keyType == "boolean") {
            Boolean value = getBoolean(keyString);
            return (T2) value;
        }
        if (keyType == "long") {
            Long value = getLong(keyString);
            return (T2) value;
        }
        if (keyType == "set") {
            Set<String> value = getSetString(keyString);
            return (T2) value;
        }
        if (keyType == "float") {
            Float value = getFloat(keyString);
            return (T2) value;
        }
        return null;
    }

    @Override
    public void apply() {
        editor.apply();
        editor.commit();
    }

    private String getType(Type type) {
        Dictionary<Type, String> typeDictionary = getTypeDictionary();
        String typeString = typeDictionary.get(type);
        if (typeString == null) {
            throw new IllegalArgumentException("type " + type.toString() + " is illegal");
        }
        return typeString;
    }

    private Dictionary<Type, String> getTypeDictionary() {
        Dictionary<Type, String> typeDictionary = new Hashtable<>();
        typeDictionary.put(Integer.class, "int");
        typeDictionary.put(Boolean.class, "boolean");
        typeDictionary.put(Long.class, "long");
        typeDictionary.put(Float.class, "float");
        typeDictionary.put(String.class, "string");
        typeDictionary.put(Set.class, "Set");
        return typeDictionary;
    }

    private String getKeyTypeString(String key) {
        return key + "Type";
    }
}
