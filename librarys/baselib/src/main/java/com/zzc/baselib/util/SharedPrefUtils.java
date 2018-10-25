package com.zzc.baselib.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zzc.baselib.base.AppBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by liqi on 2016-2-20.
 */
public class SharedPrefUtils {

    private static List<Class<?>> CLASSES = new ArrayList<Class<?>>();
    private static SharedPreferences prefs; // cache

    static {
        CLASSES.add(String.class);
        CLASSES.add(Boolean.class);
        CLASSES.add(Integer.class);
        CLASSES.add(Long.class);
        CLASSES.add(Float.class);
        CLASSES.add(Set.class);
    }

    private SharedPrefUtils() {
    }

    public static SharedPreferences getPrefs() {
        // synchronized is really needed or volatile is all I need (visibility)
        // the same instance of SharedPreferences will be returned AFAIC
        SharedPreferences result = prefs;
        if (result == null)
            synchronized (SharedPrefUtils.class) {
                result = prefs;
                if (result == null) {
                    result = prefs = PreferenceManager
                            .getDefaultSharedPreferences(AppBase.app);
                }
            }
        return result;
    }

    /**
     * <pre>
     * put(ctx, LONG_KEY, 0); // you just persisted an Integer
     * get(ctx, LONG_KEY, 0L); // CCE here
     * put(ctx, LONG_KEY, 0L); // Correct, always specify you want a Long
     * get(ctx, LONG_KEY, 0L); // OK
     * </pre>
     * <p>
     * You will get an {@link IllegalArgumentException} if the value is not an
     * instance of String, Boolean, Integer, Long, Float or Set<String> (see
     * below). This includes specifying a Double mistakenly thinking you
     * specified a Float. So :
     * <p>
     * <pre>
     * put(ctx, FLOAT_KEY, 0.0); // IllegalArgumentException, 0.0 it's a Double
     * put(ctx, FLOAT_KEY, 0.0F); // Correct, always specify you want a Float
     * </pre>
     * <p>
     * You will also get an IllegalArgumentException if you are trying to add a
     * Set<String> before API 11 (HONEYCOMB). You **can** persist a {@link Set}
     * that does not contain Strings using this method, but you are recommended
     * not to do so. It is untested and the Android API expects a Set<String>.
     * You can actually do so in the framework also but you will have raw and
     * unchecked warnings. Here you get no warnings - you've been warned. TODO :
     * clarify/test this behavior
     * <p>
     * Finally, adding null values is supported - but keep in mind that:
     * <ol>
     * <li>you will get a NullPointerException if you put a null Boolean, Long,
     * Float or Integer and you then get() it and assign it to a primitive
     * (boolean, long, float or int). This is *not* how the prefs framework
     * works - it will immediately throw NullPointerException (which is better).
     * TODO : simulate this behavior</li>
     * <p>
     * <li>you can put a null String or Set - but you will not get() null back
     * unless you specify a null default. For non null default you will get this
     * default back. This is in tune with the prefs framework</li>
     * </ol>
     *
     * @param key   the preference's key, must not be {@code null}
     * @param value an instance of String, Boolean, Integer, Long, Float or
     *              Set<String> (for API >= HONEYCOMB)
     * @throws IllegalArgumentException if the value is not an instance of String, Boolean, Integer,
     *                                  Long, Float or Set<String> (including the case when you
     *                                  specify a double thinking you specified a float, see above)
     *                                  OR if you try to add a Set<String> _before_ HONEYCOMB API
     * @throws NullPointerException     if key is {@code null}
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public static <T> void put(String key, T value) {
        SharedPreferences.Editor ed = _put(key, value);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            ed.apply();
        } else {
            ed.commit();
        }
    }

    /**
     * @param key   the preference's key, must not be {@code null}
     * @param value an instance of String, Boolean, Integer, Long, Float or
     *              Set<String> (for API >= HONEYCOMB)
     * @return true if the commit succeeded, false if not
     * @throws IllegalArgumentException if the value is not an instance of String, Boolean, Integer,
     *                                  Long, Float or Set<String> (including the case when you
     *                                  specify a double thinking you specified a float, see put())
     *                                  OR if you try to add a Set<String> _before_ HONEYCOMB API
     * @throws NullPointerException     if key is {@code null}
     */
    public static <T> boolean commit(String key, T value) {
        return _put(key, value).commit();
    }

    @SuppressLint("CommitPrefEdits")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static <T> SharedPreferences.Editor _put(String key, T value) {
        if (key == null) {
            throw new NullPointerException("Null keys are not permitted");
        }
        SharedPreferences.Editor ed = getPrefs().edit();
        if (value == null) {
            // commit it as that is exactly what the API does (but not for boxed
            // primitives) - can be retrieved as anything but if you give get()
            // a default non null value it will give this default value back
            ed.putString(key, null);
            // btw the signature is given by the compiler as :
            // <Object> void
            // gr.uoa.di.android.helpers.AccessPreferences.put(Context ctx,
            // String key, Object value)
            // if I write AccessPreferences.put(ctx, "some_key", null);
        } else if (value instanceof String) {
            ed.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            ed.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            // while int "is-a" long (will be converted to long) Integer IS NOT
            // a
            // Long (CCE) - so the order of "instanceof" checks does not matter
            // -
            // except for frequency I use the values (so I put String, Boolean
            // and
            // Integer first as I mostly use those preferences)
            ed.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            ed.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            ed.putFloat(key, (Float) value);
        } else if (value instanceof Set) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                throw new IllegalArgumentException(
                        "You can add sets in the preferences only after API "
                                + Build.VERSION_CODES.HONEYCOMB);
            }
            @SuppressWarnings({"unchecked", "unused"})
            // this set can contain whatever it wants - don't be fooled by the
                    // Set<String> cast
                    SharedPreferences.Editor dummyVariable = ed.putStringSet(key, (Set<String>) value);
        } else {
            throw new IllegalArgumentException("The given value : " + value
                    + " cannot be persisted");
        }
        return ed;
    }

    /**
     * Wrapper around {@link SharedPreferences.Editor}
     * {@code get()} methods. Null keys are not permitted. Attempts to retrieve
     * a preference with a null key will throw NullPointerException. As far as
     * the type system is concerned T is of the type the variable that is to
     * receive the default value is. You will get a {@link ClassCastException}
     * if you put() in a value of type T and try to get() a value of different
     * type Y - except if you specify a null default *where you will get the CCE
     * only if you try to assign the get() return value to a variable of type Y,
     * _in the assignment_ after get() returns*. So don't do this :
     * <p>
     * <pre>
     * AccessPreferences.put(ctx, BOOLEAN_KEY, DEFAULT_BOOLEAN);
     * AccessPreferences.get(ctx, BOOLEAN_KEY, DEFAULT_STRING); // CCE !
     * AccessPreferences.get(ctx, BOOLEAN_KEY, null); // NO CCE !!! (***)
     * String dummy = AccessPreferences.get(ctx, BOOLEAN_KEY, null); // CCE
     * </pre>
     * <p>
     * This is unlike the Preferences framework where you will get a
     * ClassCastException even if you specify a default null value:
     * <p>
     * <pre>
     * ed.putBoolean(BOOLEAN_KEY, DEFAULT_BOOLEAN);
     * ed.commit();
     * prefs.getString(BOOLEAN_KEY, null); // CCE - unlike AccessPreferences!
     * prefs.getString(BOOLEAN_KEY, &quot;a string&quot;); // CCE
     * </pre>
     * <p>
     * TODO : correct this (***)
     * <p>
     * If you put a Set<?> you will get it out as a set of strings - I am not
     * entirely clear on this
     *
     * @param key          the preference's key, must not be {@code null}
     * @return
     * @throws IllegalArgumentException if a given default value's type is not among the accepted
     *                                  classes for preferences or if a Set is given as default or
     *                                  asked for before HONEYCOMB API
     * @throws IllegalStateException    if I can't figure out the class of a value retrieved from
     *                                  preferences (when default is null)
     * @throws NullPointerException     if key is {@code null}
     */
    @SuppressWarnings("unchecked")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static <T> T get(String key) {
        return get(key, null);
    }

    @SuppressWarnings("unchecked")
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static <T> T get(String key, T defaultValue) {
        if (key == null) {
            throw new NullPointerException("Null keys are not permitted");
        }
        SharedPreferences preferences = getPrefs();
        if (defaultValue == null) {
            if (!preferences.contains(key))
                return null;
            // if the key does exist I get the value and..
            Object value = preferences.getAll().get(key);
            // ..if null I return null - here I differ from framework - I return
            // null for boxed primitives
            if (value == null)
                return null;
            // ..if not null I get the class of the non null value. Here I
            // differ from framework - I do not throw if the (non null) value is
            // not of the type the variable to receive it is - cause I have no
            // way to guess the return value expected ! (***)
            Class<?> valueClass = value.getClass();
            // the order of "instanceof" checks does not matter - still if I
            // have a long autoboxed as Integer ? - tested in
            // testAPNullDefaultUnboxingLong() and works OK (long 0L is
            // autoboxed as long)
            for (Class<?> cls : CLASSES) {
                if (valueClass.isAssignableFrom(cls)) {
                    return (T) valueClass.cast(value);
                }
            }
            // that's really Illegal State I guess
            throw new IllegalStateException("Unknown class for value :\n\t"
                    + value + "\nstored in preferences");
        } else if (defaultValue instanceof String) {// the order should not
            // matter
            return (T) preferences.getString(key, (String) defaultValue);
        } else if (defaultValue instanceof Boolean) {
            return (T) (Boolean) preferences.getBoolean(key,
                    (Boolean) defaultValue);
        } else if (defaultValue instanceof Integer) {
            return (T) (Integer) preferences.getInt(key,
                    (Integer) defaultValue);
        } else if (defaultValue instanceof Long) {
            return (T) (Long) preferences.getLong(key, (Long) defaultValue);
        } else if (defaultValue instanceof Float) {
            return (T) (Float) preferences
                    .getFloat(key, (Float) defaultValue);
        } else if (defaultValue instanceof Set) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
                throw new IllegalArgumentException(
                        "You can add sets in the preferences only after API "
                                + Build.VERSION_CODES.HONEYCOMB);
            }
            // this set can contain whatever it wants - don't be fooled by the
            // Set<String> cast
            return (T) preferences.getStringSet(key,
                    (Set<String>) defaultValue);
        } else {
            throw new IllegalArgumentException(defaultValue
                    + " cannot be persisted in SharedPreferences");
        }
    }

    /**
     * Wraps {@link SharedPreferences#contains(String)}.
     *
     * @param key the preference's key, must not be {@code null}
     * @return true if the preferences contain the given key, false otherwise
     * @throws NullPointerException if key is {@code null}
     */
    public static boolean contains(String key) {
        if (key == null) {
            throw new NullPointerException("Null keys are not permitted");
        }
        return getPrefs().contains(key);
    }

    /**
     * Wraps {@link SharedPreferences#getAll()}. Since you must
     * not modify the collection returned by this method, or alter any of its
     * contents, this method returns an <em>unmodifiableMap</em> representing
     * the preferences.
     *
     * @return an <em>unmodifiableMap</em> containing a list of key/value pairs
     * representing the preferences
     * @throws NullPointerException as per the docs of getAll() - does not say when
     */
    public static Map<String, ?> getAll() {
        return Collections.unmodifiableMap(getPrefs().getAll());
    }

    /**
     * Wraps {@link SharedPreferences.Editor#clear()}. See its
     * docs for clarifications. Calls
     * {@link SharedPreferences.Editor#commit()}
     *
     * @return true if the preferences were successfully cleared, false
     * otherwise
     */
    public static boolean clear() {
        return getPrefs().edit().clear().commit();
    }

    /**
     * Wraps {@link SharedPreferences.Editor#remove(String)}.
     * See its docs for clarifications. Calls
     * {@link SharedPreferences.Editor#commit()}.
     *
     * @param key the preference's key, must not be {@code null}
     * @return true if the key was successfully removed, false otherwise
     * @throws NullPointerException if key is {@code null}
     */
    public static boolean remove(String key) {
        if (key == null) {
            throw new NullPointerException("Null keys are not permitted");
        }
        return getPrefs().edit().remove(key).commit();
    }

    /**
     * @param lis the listener, must not be null
     * @throws NullPointerException if lis is {@code null}
     */
    public static void registerListener(
            SharedPreferences.OnSharedPreferenceChangeListener lis) {
        if (lis == null) {
            throw new NullPointerException("Null listener");
        }
        getPrefs().registerOnSharedPreferenceChangeListener(lis);
    }

    /**
     * @param lis the listener, must not be null
     * @throws NullPointerException if lis is {@code null}
     */
    public static void unregisterListener(
            SharedPreferences.OnSharedPreferenceChangeListener lis) {
        if (lis == null) {
            throw new NullPointerException("Null listener");
        }
        getPrefs().unregisterOnSharedPreferenceChangeListener(lis);
    }

    /**
     * Wraps
     * {@link SharedPreferences.OnSharedPreferenceChangeListener#onSharedPreferenceChanged(SharedPreferences, String)}
     * .
     *
     * @param lis the listener, must not be null
     * @param key the key we want to run onSharedPreferenceChanged on, must not
     *            be null
     * @throws NullPointerException if lis or key is {@code null}
     */
    public static void callListener(
            SharedPreferences.OnSharedPreferenceChangeListener lis, String key) {
        if (lis == null) {
            throw new NullPointerException("Null listener");
        }
        if (key == null) {
            throw new NullPointerException("Null keys are not permitted");
        }
        lis.onSharedPreferenceChanged(getPrefs(), key);
    }

    /**
     * Check that the given set contains strings only.
     *
     * @param set
     * @return the set cast to Set<String>
     */
    @SuppressWarnings("unused")
    private static Set<String> checkSetContainsStrings(Set<?> set) {
        if (!set.isEmpty()) {
            for (Object object : set) {
                if (!(object instanceof String)) {
                    throw new IllegalArgumentException(
                            "The given set does not contain strings only");
                }
            }
        }
        @SuppressWarnings("unchecked")
        Set<String> stringSet = (Set<String>) set;
        return stringSet;
    }

    /**
     * 保存List
     *
     * @param tag
     * @param dataList
     */
    public static <T> void setDataList(String tag, List<T> dataList) {
        if (null == dataList || dataList.size() <= 0)
            return;
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(dataList);
        put(tag, strJson);
    }

    /**
     * 获取List
     *
     * @param tag
     * @return
     */
    public static <T> List<T> getDataList(String tag) {
        List<T> dataList = new ArrayList<>();
        String strJson = prefs.getString(tag, null);
        if (null == strJson) {
            return dataList;
        }
        Gson gson = new Gson();
        dataList = gson.fromJson(strJson, new TypeToken<List<T>>() {
        }.getType());
        return dataList;
    }
}
