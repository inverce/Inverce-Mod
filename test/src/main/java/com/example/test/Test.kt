//package com.example.test
//
//import android.os.Bundle
//import android.support.annotation.CheckResult
//import android.support.annotation.IdRes
//import android.support.annotation.RestrictTo
//import android.support.v4.app.Fragment
//import android.support.v4.app.FragmentActivity
//import android.support.v4.app.FragmentManager
//import com.inverce.mod.core.IM
//import com.inverce.mod.core.Log
//import com.inverce.mod.core.R
//import com.inverce.mod.core.functional.IFunction
//import com.inverce.mod.core.functional.ISupplierEx
//import com.inverce.mod.core.verification.Preconditions
//import com.inverce.mod.navigation.Back
//import com.inverce.mod.navigation.Forward
//import com.inverce.mod.navigation.Navigator
//import com.inverce.mod.navigation.Replace
//import com.inverce.mod.navigation.Reset
//import java.util.ArrayList
//import java.util.concurrent.TimeUnit
//
//
//
//object Navigator {
//
//    @CheckResult fun on(activity: FragmentActivity): ActionCreateAll
//            = on({ activity.supportFragmentManager }, { activity })
//    @CheckResult fun on(manager: FragmentManager): ActionCreateAll
//            = on({manager}, null)
//    @CheckResult fun on(fragment: Fragment): ActionCreateAll
//            = on({ fragment.childFragmentManager }, { fragment.activity })
//
//    /**
//     * Returns navigator stack builder with fragment manager supplier
//     * @return builder
//     */
//    @CheckResult
//    fun on(managerSupplier: () -> FragmentManager? = {
//        val activity = IM.activitySupport()
//        activity?.supportFragmentManager
//    }, activityISupplier: (() -> FragmentActivity?)?): ActionCreateAll {
//        return ActionCreateAll(Manager(managerSupplier, activityISupplier))
//    }
//
//    fun setDefaultContainer(@IdRes defaultContainer: Int) {
//       Manager.PAGE_CONTAINER = defaultContainer
//    }
//
//    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
//    internal fun submitInvertedStack(stack: com.inverce.mod.navigation.ActionStack) {
//        var stack = stack
//        val list = ArrayList<com.inverce.mod.navigation.ActionStack>(1)
//        Preconditions.checkNotNull(stack, "Nope, null stack, is no good.")
//        list.add(stack)
//        while (stack.previous != null) {
//            list.add(stack.previous)
//            stack = stack.previous
//        }
//
//        Preconditions.checkArgument(list.size > 1, "Error. There are no actions in stack")
//        Preconditions.checkArgument(list[0] is Manager, "Error. Manager not found")
//        val manager = list[0] as Manager
//        list.removeAt(0)
//        IM.onUi().execute { submitStack(manager, list) }
//    }
//
//    protected fun submitStack(manager: com.inverce.mod.navigation.Manager, stack: List<com.inverce.mod.navigation.ActionStack>) {
//        val fm = manager.managerSupplier.get()
//        Preconditions.checkNotNull(fm, "Error. Fragment manager is not present")
//
//        for (action in stack) {
//            if (action is Back) {
//                handleBack(manager, fm, action)
//            } else if (action is Reset) {
//                handleReset(manager, fm, action)
//            } else if (action is Replace) {
//                handleReplace(manager, fm, action)
//            } else if (action is Forward) {
//                handleForward(manager, fm, action)
//            } else {
//                Log.w("Unknown action: " + action)
//            }
//        }
//    }
//
//    private fun handleForward(manager: com.inverce.mod.navigation.Manager, fm: FragmentManager, action: Forward) {
//        val fragment = action.fragmentSupplier.get()
//        val tag = manager.tagSupplier.apply(fragment)
//        val backStackName = manager.nameSupplier.apply(fragment)
//        fm
//                .beginTransaction()
//                .replace(manager.container, fragment, tag)
//                .addToBackStack(backStackName)
//                .commit()
//    }
//
//    private fun handleReplace(manager: com.inverce.mod.navigation.Manager, fm: FragmentManager, action: Replace) {
//        val fragment = action.fragmentSupplier.get()
//        val tag = manager.tagSupplier.apply(fragment)
//        val backStackName = manager.nameSupplier.apply(fragment)
//        fm
//                .beginTransaction()
//                .replace(manager.container, fragment, tag)
//                .addToBackStack(backStackName)
//                .commit()
//
//        // remove last
//        if (fm.backStackEntryCount > 0) {
//            fm.popBackStackImmediate()
//        }
//        // move to state
//        handleForward(manager, fm, action)
//    }
//
//    private fun handleReset(manager: com.inverce.mod.navigation.Manager, fm: FragmentManager, action: Reset) {
//        for (i in 0..fm.backStackEntryCount - 1) {
//            fm.popBackStack()
//        }
//        fm.executePendingTransactions()
//    }
//
//    private fun handleBack(manager: com.inverce.mod.navigation.Manager, fm: FragmentManager, back: Back) {
//        if (back.count == 1 && back.toClass == null && back.toTag == null) {
//            manager.activitySupplier.get().onBackPressed()
//            return
//        }
//
//        for (i in 0..fm.backStackEntryCount - 1) {
//            var clear = false
//            val top = fm.findFragmentById(manager.container)
//            if (back.count > 0) {
//                back.count--
//                clear = true
//            }
//            clear = clear or (top != null && back.toTag != null && back.toTag != top.tag)
//            clear = clear or (top != null && back.toClass != null && !back.toClass.isInstance(top))
//
//            if (clear) {
//                fm.popBackStack()
//            } else {
//                break
//            }
//        }
//        fm.executePendingTransactions()
//    }
//}
//
//open class ActionStackFinalize internal constructor(previous: com.inverce.mod.navigation.ActionStack) : com.inverce.mod.navigation.ActionStack(previous) {
//
//    fun after(time: Long, unit: TimeUnit) {
//        IM.onBg().schedule({ this.commit() }, time, unit)
//    }
//
//    fun commit() {
//        Navigator.submitInvertedStack(this)
//    }
//
//    @CheckResult
//    fun and(): ActionCreate {
//        return ActionCreate(previous)
//    }
//}
//class ActionCreateFinalize internal constructor(previous: ActionStack) : ActionCreate(previous) {
//    fun after(time: Long, unit: TimeUnit) = IM.onBg().schedule({ this.commit() }, time, unit)
//    fun commit() = Navigator.submitInvertedStack(this)
//    @CheckResult fun and(): ActionCreate = ActionCreate(previous)
//
//}
//class Replace internal constructor(previous: ActionStack, fragmentSupplier: ISupplierEx<Fragment>) : Forward(previous, fragmentSupplier)
//class Reset internal constructor(stack: ActionStack) : ActionStackFinalize(stack)
//class Back internal constructor(stack: ActionStack) : ActionStackFinalize(stack) {
//    internal var count = 1
//    internal var toTag: String? = null
//    internal var toClass: Class<*>? = null
//
//    @CheckResult
//    fun twice(): ActionStackFinalize {
//        return times(2)
//    }
//
//    @CheckResult
//    operator fun times(count: Int): ActionStackFinalize {
//        this.count = count
//        return ActionStackFinalize(this)
//    }
//
//    @CheckResult
//    fun to(tag: String): ActionStackFinalize {
//        this.toTag = tag
//        return ActionStackFinalize(this)
//    }
//
//    @CheckResult
//    fun to(clazz: Class<*>): ActionStackFinalize {
//        this.toClass = clazz
//        return ActionStackFinalize(this)
//    }
//}
//
//open class Forward internal constructor(previous: ActionStack, fragmentSupplier: ISupplierEx<Fragment>) : ActionStackFinalize(previous) {
//    internal var fragmentSupplier: ()-> Fragment?
//
//    init {
//        this.fragmentSupplier = {
//            try {
//                fragmentSupplier.get()
//            } catch (e: Exception) {
//                null
//            }
//        }
//    }
//}
//
//internal class Manager(var managerSupplier: () -> FragmentManager?, activitySupplier: (() -> FragmentActivity?)?) : ActionStack(null) {
//    var activitySupplier: () -> FragmentActivity?
//    var tagSupplier: (Fragment) -> String? = { p -> null }
//    var nameSupplier: (Fragment) -> String? = { p -> null }
//    var container = PAGE_CONTAINER
//
//    init {
//        this.activitySupplier = { activitySupplier?.invoke() ?: IM.activitySupport() }
//    }
//
//    companion object {
//        var PAGE_CONTAINER = R.id.page_container
//    }
//}
//
//open class ActionStack internal constructor(protected var previous: ActionStack?) {
//    internal fun findManager(): Manager? {
//        return if (this is Manager) this else previous?.findManager()
//    }
//}
//
//class ActionCreateAll internal constructor(previous: ActionStack) : ActionCreate(previous) {
//    internal var manager: Manager
//
//    init {
//        this.manager = findManager()
//    }
//
//    @CheckResult fun back(): Back = Back(this)
//    @CheckResult fun reset(): Reset = Reset(this)
//    @CheckResult fun into(@IdRes container: Int): ActionCreateAll {
//        manager.container = container
//        return this
//    }
//
//    @CheckResult
//    fun setBackStackNameProvider(supplier: IFunction<Fragment, String>?): ActionCreateAll {
//        var supplier = supplier
//        if (supplier == null) supplier = { p -> null }
//        manager.nameSupplier = supplier
//        return this
//    }
//
//    @CheckResult
//    fun setTagProvider(supplier: IFunction<Fragment, String>?): ActionCreateAll {
//        var supplier = supplier
//        if (supplier == null) supplier = { p -> null }
//        manager.tagSupplier = supplier
//        return this
//    }
//
//    @CheckResult
//    fun setContainerId(@IdRes containerId: Int): ActionCreateAll {
//        manager.container = if (containerId != 0) containerId else Manager.PAGE_CONTAINER
//        return this
//    }
//
//
//}
//
//open class ActionCreate internal constructor(previous: ActionStack) : ActionStack(previous) {
//
//    @CheckResult fun forwardTo(supplierEx: () -> Fragment?): ActionStackFinalize {
//        return ActionStackFinalize(Forward(this, supplierEx))
//    }
//
//    @CheckResult fun forwardTo(fragment: Fragment): ActionStackFinalize = forwardTo({ fragment })
//    @CheckResult fun forwardTo(fragmentClass: Class<out Fragment>): ActionStackFinalize = forwardTo({ fragmentClass.newInstance() })
//
//    @CheckResult
//    fun forwardTo(fragmentClass: Class<out Fragment>, arguments: Bundle): ActionStackFinalize {
//        return forwardTo({
//            val fragment = fragmentClass.newInstance()
//            fragment.arguments = arguments
//            fragment
//        })
//    }
//
//    @CheckResult fun replaceWith(supplierEx: ISupplierEx<Fragment>): ActionStackFinalize = ActionStackFinalize(Replace(this, supplierEx))
//    @CheckResult fun replaceWith(fragment: Fragment): ActionStackFinalize = replaceWith({ fragment })
//    @CheckResult fun replaceWith(fragmentClass: Class<out Fragment>): ActionStackFinalize = replaceWith({ fragmentClass.newInstance() }
//    @CheckResult fun replaceWith(fragmentClass: Class<out Fragment>, arguments: Bundle): ActionStackFinalize {
//        return replaceWith({
//            val fragment = fragmentClass.newInstance()
//            fragment.arguments = arguments
//            fragment
//        })
//    }
//}