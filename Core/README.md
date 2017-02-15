## CORE

No initialization, no boilercode, utilities for android.

#### Instalation
```gradle
compile 'com.inverce.mod:Core:1.0.2'
```

### IM

1. Provide context in all forms.
  * IM.context() - current context
  * IM.activity() - current activity, null if no activity
  * IM.application() - current application
  * IM.resources() - current resources
  
2. Provide access to schedulers
  * IM.onBg() - Shared background thread pool executor. 
  * IM.onUi() - Ui thread executor.

### Logger

This is logger with only few twists for easier debugging:

 * Specify log level: `Log.setLogLevel(Log.INFO);`
 * Disable loggin completly: `Log.setDebugMode(false);`
 * Register listener:  `Log.setListener(new LogListener() { ...`
 * Ignore tags, (if no tag is specified application name will be used)
 * Write shorter exception traces: `Log.exs(exception);`
 * Easy string formating: `Log.i("Yout lucky number is: %d", 42);

### Lifecycle [MOVE PENDING]

  *  Lifecycle.getActivityState() - check in what state your current activity is
  
### UI 
  
  * Ui.isUiThread() - check whatever current thread is UiThread
  * Ui.visible(view, show, (optional) should_collapse) - easily and safely set visibility state for view
  * Ui.getInflater() - [MOVE PENDING] - get default LayoutInflater
  * Ui.hideSoftInput(view) - hide keybord provided current focus
  * Ui.runOnNextLayout(view, runnable) - run specified action, after next layout update for specified view
  * Ui.Layout / Ui.Margin - Utility classes for manipulating margins and padding of view via layout params. [REQUIRES DOCS]
  
##### To be done in relatively near future

* Move pending methods
* Add to log auto tag
* Add simple preconditions utilities
* Add configuration utilities
* Add more docs and examples
