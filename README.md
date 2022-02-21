Test Code of [https://github.com/android/performance-samples/tree/main/ConstraintLayoutPerformance](https://github.com/android/performance-samples/tree/main/ConstraintLayoutPerformance)

#### Include five type of view

Mode | Explain 
--- | ---
Empty | just View(Context), empty view for compare.
Constraint | Use ConstraintLayout for root view with flat hierarchy
Normal  | Use RelativeLayout for root view with nested hierarchy
Simple | Use RelativeLayout for root view with flat hierarchy
Custom | Use CustomViewGroup with java code, flat hierarchy
Total Custom | Same with Custom, skip some measure step

#### Usage With Source Code

1. Run app, open MainActivity.
2. Click "jump tradtion", Click "start test"
3. After a few minutes, Test Result will show in MainActivity.

#### Some Test Result (nano seconds)

|               | **Constraint** | **Normal** | **Simple** | **Custom** | **Total Custom** |
| ------------- | -------------- | ---------- | ---------- | ---------- | ---------------- |
| **create**    | 22,901,479     | 20,519,692 | 21,956,968 | 17,174,885 | 16,769,911       |
| **measure**   | 3,587,820      | 6,886,500  | 4,071,617  | 3,245,275  | 3,082,938        |
| **layout**    | 540,735        | 607,407    | 541,424    | 688,557    | 691,130          |
| **full_flow** | 27,030,034     | 28,013,599 | 26,570,009 | 21,108,717 | 20,543,979       |
| **frame**     | 4,785,884      | 7,993,213  | 5,294,644  | 4,693,871  | 4,543,181        |

1. Full_flow : create + measure + layout
2. Frame: `FrameMetrics.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION) ` , measure + layout