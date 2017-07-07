# Vision

Simples camera vision code

#### Instalation
```gradle
compile 'com.inverce.mod:Vision:1.0.6'
```

## Step 1

Add vision fragment to your xml, and find it in your code.

```xml
<fragment
      android:id="@+id/vision_fragment"
      class="com.inverce.mod.vision.VisionScannerFragment"
      android:layout_width="match_parent"
      android:layout_height="match_parent" />
```

```java
visionFragment = (VisionScannerFragment) getChildFragmentManager().findFragmentById(R.id.vision_fragment)
```

## Step 2 
Setup scanned barcodes and listener

```java
visionFragment.setBarcodeFormats(Barcode.EAN_8 | Barcode.EAN_13);
visionFragment.setDetectionListener(this);
```

## Step 3
Receive results via listener


```java
@Override
public void onNewDetection(Barcode result) {
    Log.w("Scanned: " + result.rawValue);
}
```

## Notes

When using from fragment this fragment (as any other) should be removed from childManager when fragment is destroyed

```java
@Override
public void onDestroyView() {
  super.onDestroyView();
  if (visionFragment != null) {
      getChildFragmentManager()
            .beginTransaction()
            .remove(visionFragment)
            .commitAllowingStateLoss();
  }
}
```
