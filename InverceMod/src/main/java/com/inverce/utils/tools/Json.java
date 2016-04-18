package com.inverce.utils.tools;

import android.support.annotation.NonNull;
import android.util.Pair;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

@SuppressWarnings("unused")
public class Json {
    public static JSONArray optArray(JSONObject json, String key, String... keys) {
        JSONArray array = json.optJSONArray(key);
        if (array == null) {
            for (String keyOpt : keys) {
                array = json.optJSONArray(keyOpt);
                if (array != null)
                    return array;
            }
            return new JSONArray();
        } else {
            return array;
        }
    }

    public static JSONArray optArray(JSONArray json, int key) {
        JSONArray array = json.optJSONArray(key);
        return array != null ? array : new JSONArray();
    }

    public static JSONObject optObject(JSONObject json, String key, String... keys) {
        JSONObject array = json.optJSONObject(key);
        if (array == null) {
            for (String keyOpt : keys) {
                array = json.optJSONObject(keyOpt);
                if (array != null)
                    return array;
            }
            return new JSONObject();
        } else {
            return array;
        }
    }

    public static JSONObject optObject(JSONArray json, int key) {
        JSONObject array = json.optJSONObject(key);
        return array != null ? array : new JSONObject();
    }

    public static Iterable<? extends JSONObject> iterate(final JSONArray array) {
        return new Iterable<JSONObject>() {
            @Override
            public Iterator<JSONObject> iterator() {
                return new Iterator<JSONObject>() {
                    int next = 0;

                    public boolean hasNext() {
                        return next < array.length();
                    }

                    public JSONObject next() {
                        return array.optJSONObject(next++);
                    }

                    public void remove() {
                    }
                };
            }
        };
    }

    @SuppressWarnings("unchecked")
    // user knows that we are casting, its his job to make sure its safetype
    public static <T> Iterable<? extends T> iterate(final Class<T> tClass, final JSONArray array) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new Iterator<T>() {
                    int next = 0;

                    public boolean hasNext() {
                        return next < array.length();
                    }

                    public T next() {
                        switch (tClass.getSimpleName()) {
                            case "String":
                                return (T) array.optString(next++);
                            case "Double":
                                return (T) (Double) array.optDouble(next++);
                            case "Integer":
                                return (T) (Integer) array.optInt(next++);
                            case "Boolean":
                                return (T) (Boolean) array.optBoolean(next++);
                            case "JSONObject":
                                return (T) array.optJSONObject(next++);
                            case "JSONArray":
                                return (T) array.optJSONArray(next++);
                            case "Object":
                                return (T) array.opt(next++);
                            default:
                                throw new RuntimeException(new IllegalArgumentException("Cant parse to selected type"));
                        }
                    }

                    public void remove() {
                    }
                };
            }
        };
    }

    public static Iterable<Pair<String, Object>> iterate(final JSONObject array) {
        return new Iterable<Pair<String, Object>>() {
            @Override
            public Iterator<Pair<String, Object>> iterator() {
                return new Iterator<Pair<String, Object>>() {
                    private Iterator<String> inner = array.keys();

                    public boolean hasNext() {
                        return inner.hasNext();
                    }

                    public Pair<String, Object> next() {
                        String key = inner.next();
                        return new Pair<>(key, array.opt(key));
                    }

                    public void remove() {
                    }
                };
            }
        };
    }

    @SuppressWarnings("unchecked")
    // user knows that we are casting, its his job to make sure its safetype
    public static <T> Iterable<Pair<String, T>> iterate(final Class<T> tClass, final JSONObject array) {
        return new Iterable<Pair<String, T>>() {
            @Override
            public Iterator<Pair<String, T>> iterator() {
                return new Iterator<Pair<String, T>>() {
                    private Iterator<String> inner = array.keys();

                    public boolean hasNext() {
                        return inner.hasNext();
                    }

                    public Pair<String, T> next() {
                        String key = inner.next();

                        switch (tClass.getSimpleName()) {
                            case "String":
                                return new Pair<>(key, (T) array.optString(key));
                            case "Double":
                                return new Pair<>(key, (T) (Double) array.optDouble(key));
                            case "Integer":
                                return new Pair<>(key, (T) (Integer) array.optInt(key));
                            case "Boolean":
                                return new Pair<>(key, (T) (Boolean) array.optBoolean(key));
                            case "JSONObject":
                                return new Pair<>(key, (T) array.optJSONObject(key));
                            case "JSONArray":
                                return new Pair<>(key, (T) array.optJSONArray(key));
                            case "Object":
                                return new Pair<>(key, (T) array.opt(key));
                            default:
                                throw new RuntimeException(new IllegalArgumentException("Cant parse to selected type"));
                        }
                    }

                    public void remove() {
                    }
                };
            }
        };
    }

    @NonNull
    public static JSONObject optObject(String json) {
        try {
            return new JSONObject(json);
        } catch (Exception ex) {
            return new JSONObject();
        }
    }

    public static double optDouble(JSONObject json, String name, double onError) {
        try {
            return json.getDouble(name);
        } catch (Exception ignored) {
        }
        try {
            return Double.parseDouble(json.getString(name).replace(",", "."));
        } catch (Exception ignored) {
        }
        return onError;
    }
}
