# Events

#### Instalation
```gradle
compile 'com.inverce.mod:Events:1.0.0'
```



## Event.Bus

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
 
### How to
1. Declare your listener:

    ```java
    public interface UserLoggedEvent extends Listener {
       void userStateChanged(boolean logged);
    }
   ```
   
2. Register and unregister events as needed
    ```java
    Event.Bus.register(UserLoggedEvent.class, {listener});
    Event.Bus.unregister(UserLoggedEvent.class, {listener});
    ```
  
3. Post events via `post`:

    ```java
    Event.Bus.post(UserLoggedEvent.class)
         .userStateChaged(true);
    ```

## Event\<T\>

Allows user to manage single event.

### How to

1. Declare your listener:

    ```java
    public interface OnUserEatListener extends Listener {
       void userEatCake(int pieces);
       void userEatPudding();
    }
   ```

2. Declare event handler

   ```java
    private Event<OnUserEatListener> userEatEvent = new Event<>(OnUserEatListener.class);
   ```

3. Allow registration of events via provided interfaces.
  
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
    
    
## To be done in relatively near future

* Allow multi registration, or simply without providing event class
* Add local bus for user to manage
* Add priorities, and ability to specify thread per subscribed instance method
* Cache annotations
* Add more docs and examples
