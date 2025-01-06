# EzyLoaders - Android UI Loader Balls Library
---

## Overview

The **Android UI Loader Balls Library** is a versatile and highly customizable library designed to provide developers with sleek, animated UI loader views for Android applications. With a variety of pre-designed loader views, developers can integrate visually appealing loading indicators seamlessly into their projects.

<img src="https://github.com/user-attachments/assets/fa72b31a-9b89-4c30-bac1-a54b3b922b92" alt="drawing" width="100"/>
<img src="https://github.com/user-attachments/assets/2d8608fa-ed61-45af-b38a-0721af3a6c83" alt="drawing" width="100"/>
<img src="https://github.com/user-attachments/assets/278d7ebd-3edc-4233-9e2a-9c7f4e55353a" alt="drawing" width="100"/>
<img src="https://github.com/user-attachments/assets/b4c52e97-4499-462e-8597-660e2181149b" alt="drawing" width="100"/>
<img src="https://github.com/user-attachments/assets/60f41fd6-0bf1-47dd-992f-e229d8c76c1d" alt="drawing" width="100"/>


### Features

- **Customizable Views**: Adjust color, size, speed, and other attributes to fit your design requirements.
- **Easy Integration**: Add loaders directly in XML with minimal setup.
- **Diverse Loader Options**: Choose from multiple loader styles such as Circular Loader, Line Spinner, Dot Pulse, and more.
- **Lightweight**: Optimized for performance without compromising visual quality.

## Badges

![Jetpack Version](https://img.shields.io/badge/Jetpack-1.0.2-brightgreen) ![License](https://img.shields.io/badge/License-Apache%202.0-blue) ![Languages](https://img.shields.io/github/languages/top/Harry2876/EzyLoaders) ![Stars](https://img.shields.io/github/stars/Harry2876/EzyLoaders?style=social) ![Forks](https://img.shields.io/github/forks/Harry2876/EzyLoaders?style=social)


### Features
- 🚀 **Customizable Views**: Adjust color, size, speed, and other attributes to fit your design.
- 🎨 **Easy Integration**: Add loaders directly in XML with minimal setup.
- 🛠️ **Lightweight**: Optimized for performance.


| Feature               | UI Loader Balls | Default ProgressBar | Other Libraries |
|-----------------------|-----------------|---------------------|-----------------|
| Customizable          | ✅              | ❌                  | ✅              |
| Multiple Loaders      | ✅              | ❌                  | ✅              |
| Lightweight           | ✅              | ✅                  | ❌              |

---

## Installation

### Step 1: Add Dependency

Add the following dependency to your app's `build.gradle` file:

```gradle
implementation("com.github.Harry2876:EzyLoaders:$version")
```


### Step 2: Sync Gradle

Sync your Gradle project to ensure the library is added.

---

## Usage

### XML Integration

Include any loader view in your layout XML file. Each loader style comes with customizable attributes for easy configuration.

### Available Loaders and Attributes

Below are the available loaders with their respective attributes:

### 1. CircularLoaderView

```xml
<com.hariomharsh.loaders.ui.CircularLoaderView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:circle_color="@color/primaryColor"
    app:circle_size="48dp"
    app:circle_speed="150"
    app:circle_stroke="4dp" />
```

**Attributes:**

- `circle_color`: Sets the color of the circle.
- `circle_size`: Defines the size of the circle.
- `circle_speed`: Adjusts the animation speed.
- `circle_stroke`: Specifies the thickness of the circle stroke.

### 2. LineSpinnerView

```xml
<com.hariomharsh.loaders.ui.LineSpinnerView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:line_color="@color/secondaryColor"
    app:line_size="64dp"
    app:line_stroke="3dp"
    app:line_speed="200" />
```

**Attributes:**

- `line_color`: Sets the color of the spinner lines.
- `line_size`: Defines the overall size of the spinner.
- `line_stroke`: Adjusts the thickness of the lines.
- `line_speed`: Controls the animation speed.

### 3. TailChaseSpinner

```xml
<com.hariomharsh.loaders.ui.TailChaseSpinner
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:spinner_color="@color/accentColor"
    app:spinner_size="50dp"
    app:spinner_dot_size="1.5" />
```

**Attributes:**

- `spinner_color`: Specifies the color of the tail spinner.
- `spinner_size`: Defines the size of the spinner.
- `spinner_dot_size`: Sets the size of the dots.

### 4. ThreeDotsPyramid

```xml
<com.hariomharsh.loaders.ui.ThreeDotsPyramid
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:pyramid_color="@color/primaryDarkColor"
    app:pyramid_size="60dp" />
```

**Attributes:**

- `pyramid_color`: Sets the color of the pyramid dots.
- `pyramid_size`: Defines the size of the pyramid.

### 5. DotPulse

```xml
<com.hariomharsh.loaders.ui.DotPulse
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:dot_color="@color/highlightColor"
    app:dot_size="12dp"
    app:dot_spacing="8dp"
    app:dot_count="3"
    app:dot_speed="300" />
```

**Attributes:**

- `dot_color`: Sets the color of the dots.
- `dot_size`: Specifies the size of the dots.
- `dot_spacing`: Adjusts the spacing between dots.
- `dot_count`: Defines the number of dots.
- `dot_speed`: Controls the speed of the animation.

---

## Author

Developed and maintained by Hariom Harsh

## FAQ
**Q: Is this library compatible with older Android versions?**  
A: Yes, it supports Android API 21+.

**Q: Can I use this with Jetpack Compose?**  
A: Not yet, but Compose support is planned for future releases.

---


## License
Copyright 2024 Hariom Harsh

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at:

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Any usage of this library must include proper attribution to the original author.
