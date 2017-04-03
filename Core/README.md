# CORE

No initialization, no boilercode, utilities for android.

#### Instalation
```gradle
compile 'com.inverce.mod:Core:1.0.3'
```

### IM \<Class\>

Provides easy access to context and executors.

##### Context
  * context() - current context, this will be either activity or application based on whats available.
  * activity() - last activity, null if no activity, was present or last activity has been collected by GC.
  * application() - current application context
  * resources() - current resources from contex()
  * inflater() - shortcut for LayoutInflater.from(IM.context());

##### Schedulers
  * onBg() - Shared background thread pool executor. With no maximum thread count and keep alive time set to 1 sek. Easies way to execute something on background thread.
    * run code on bg thread
    ```java
    IM.onBg().execute(() -> { /* here are dragons on BG thread */ });
    ```
    * schedule for delayed execution
    ```java
    IM.onBg().schedule(() -> { /* Dragons will come back after 400 years */ }, 400, TimeUnit.YEARS);
    ```
  * onUi() - Ui (MainThread) executor. Backed by android thread handler.
    * execute code on ui
    ```java
    IM.onUi().execute(() -> { /* here are dragons on UI thread */ });
    ```
    
### Logger \<Class\>

Wrapper around android Log class, with additionnal benefits.

* Implements all Log methods, adds some more for easier code use.
 ```java
    Log.w("Hello World");
 ```
* Allows user to specify minimum log level to print.
  This makes it possible to reduce log messages in production environment without removing all messages.
 ```java
    Log.setLogLevel(Log.INFO);
 ```
* Easily disable logging.
 ```java
    Log.setDebugMode(false);
 ```
* Allows to register listeners.
 ```java
    Log.setListener(new LogListener() {
            @Override
            public boolean handleMsg(int lvl, String tag, String msg) {
                // handle normal message
                return false; // return true if you don't want message to be print
            }

            @Override
            public boolean handleExc(int simple_lvl, String tag, String msg, Throwable o) {
                // handle exception message
                return false; // return true if you don't want message to be print
            }
        });
 ```
* Write what you need, no clutter, not mandatory tags
 ```java
    Log.d("It's me Mario."); // omit tag, will use app name as tag
    Log.d("I have %d castles.", 10); // automatically parse String.format messages
    Log.d("Mario", "The princess is in another castle"); // use tag if you want to
 ```
* Simplify exception traces.
 ```java
    Log.exs(exception); // writes only trace information in your package.
    Log.exs("Message", exception); // allow for additional message.
 ``` 
* All methods from android log are present so switching from android to IM is quite easy
 ```diff
    - import android.util.Log;
    + import com.inverce.mod.core.Log;
 ```
### Events 

INFO: Events are now part of core library.

### Event.Bus

Well its just another implementation of publish/subscribe event bus. 

Using events in your aplication provides following adventages:
 * less code... less boiler code
 * easy communication between components
 * decouples event senders and receivers

Pros:
 * Simple and easy to use
 * No memory leaks even if you forget to unregister
 * Allows posting events on Ui, Bg or same thread as calling method.
 * All listeners are defined by interface, its easy to find and modify calls.
 * No code gen or hidden serialization
 * You can create your own methods to invoke
 * its fast
 * super small aar (~25 kb)
 
Cons
 * By default events are posted on the same thread
 * Library uses reflection api
 * Even if many listeners are provided only value from first will be returned
 * its not as fast as code gen
 
#### How to
Declare your listener:
```java
public interface UserLoggedEvent extends Listener {
   void userStateChanged(boolean logged);
}
```
Register and unregister events as needed
```java
Event.Bus.register(UserLoggedEvent.class, {listener});
Event.Bus.unregister(UserLoggedEvent.class, {listener});
```
Post events via `post`:
```java
Event.Bus.post(UserLoggedEvent.class)
     .userStateChaged(true);
```

### Event\<T\>

Allows user to manage single event. ## REQUIRES MORE DOCS

#### How to

Declare your listener:
    
```java
public interface OnUserEatListener extends Listener {
    void userEatCake(int pieces);
    void userEatPudding();
}
```
Declare event handler

```java
private Event<OnUserEatListener> userEatEvent = new Event<>(OnUserEatListener.class);
```

Optionally: Allow registration of events via provided interfaces.  
Event implements two interfaces to modify listeners and expose for user:  
* MultiEvent\<T\> - allow registration of multiple listeners, and provides following methods
   * void addListener(T listener);
   * void removeListener(T listener);
   * void clear();
* SimpleEvent\<T\> - keeps only last registration of listener, and provides following methods
   * void setListener(T listener);
   
```java
public MultiEvent<T> getUserEatEvent() {
    return userEatEvent;
}
```

3. Post your callbacks via provided proxy claass

```
userEatEvent.post().userEatCake(1);
```



### Lifecycle \<Class\>

* getActivityState() - return current activity state
 ```java
    if (Lifecycle.getActivityState() == ActivityState.Resumed) {
       // ok so... activity is resumed
    }
 ``` 
* setListener - listen for activity state changes
 ```java
    Lifecycle.setListener(new ActivityStateListener() {
        @Override
        public void activityStateChanged(ActivityState state, Activity activity, Bundle extra) {
              
        }
    });
 ``` 
* setPostEvents(boolean) - sets whatever Lifecycle class should post ActivityStateListener event, with Event.Bus each time state changes
 ```java
    Lifecycle.setPostEvents(true);
 ```
 
### UI \<Class\>
  
  * isUiThread() - check whatever current thread is MainThread (UI)
  * visible(view, show, (optional) should_collapse) - easily and safely set visibility state for view
  ```java
     IM.visible(specialOfferBtn, element.isSpecial()); 
  ```
  * hideSoftInput(view) - hide soft keybord, requires specifying focused view
  * runOnNextLayout(view, runnable) - run specified action, after next layout update for specified view.
  * Layout / Margin - Utility classes for LayoutParams.
  ```java
     Ui.Margin.on(view)
         .top(1, true) // 1 px
         .topPadding(10, false) // 10 Dp
         .height(100, true) // 100 px
         .done(); // call to reapply LayoutParams
  ```
  * makeSelector(drawable, pressed, disabled) - creates simple selector with color overlays for presset and disabled state

### Configuration \<Package\>

This package provides configuration utilities.

### Collections \<Package\>

#### CacheFunctionMap\<Key, Value\> \<Class\>
Allows to easily cache function results. ## REQUIRES DOCS

#### FlatArrayList\<E\> \<Class\>
Flattens multiple list into one, while allowing change in subLists.

```java
List<Integer>
        A = Arrays.asList(1, 2, 3),
        B = Arrays.asList(11, 12, 13);
ArrayList<Integer> C = new ArrayList<>();

FlatArrayList<Integer> flatList = new FlatArrayList<>();

flatList.store().add(A);
flatList.store().add(C);

// flatList = [ 1, 2, 3]

flatList.store().add(B);

// flatList = [ 1, 2, 3, 11, 12, 13]

C.add(120);

// flatList = [ 1, 2, 3, 120, 11, 12, 13]

flatList.store().remove(A);

// flatList = [ 120, 11, 12, 13]
```
 
#### TreeNode \<Class\>
Node implementation for tree structure, allows traversal view TraverseTreeCollection. ## REQUIRES DOCS

#### WeakArrayList\<E\> \<Class\>
Implementation of List backed by ArrayList\<WeakReference\<E\>\>.

WeakArrayList does NOT remove weak references from collection automatically.  
Thus .get(position) may return null value if GC, has collected value at specified position.  
This will not change size of collection. To clear collection of empty references use:

  * clearEmptyReferences() - clear all values collected by GC
 


