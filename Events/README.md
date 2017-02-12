### Events by Inverce

##### 1. Event\<T\>

Allows user to declare event listeners more easily.


Declare your listener:
```
public interface OnUserEatListener extends Listener {
  void userEatCake(int pieces);
  void userEatPudding();
}
```

Then declare event handler
```
private Event<OnUserEatListener> userEatEvent = new Event<>(OnUserEatListener.class);
```

Event implements two interfaces to modify listeners and expose for user:

1. MultiEvent<T> - allow registration of multiple listeners
2. Simple<Event<T> - keeps only last registration of listener

Post your callbacks via provided proxy claass
```
userEatEvent.post().userEatCake(1);
```

##### 2. Event.Bus
