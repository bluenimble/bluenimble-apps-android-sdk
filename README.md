# BlueNimble Apps SDK for Android

**BlueNimble Apps SDK** let you create android applications **almost without writing a single line of code**. You can create complex pages, styling components, adding effects and integrate with your backend using simple json configuration files.

With almost no android experience, you can create advanced applications without the hassle of understanding how layouts, visual components, async tasks or backend integration works in android.

> **BlueNimble Apps SDK** sdk provides android developers with simple java interfaces which they can extend to create new themes, component types, effects, data binding, backend integration and more

Getting Started
-----

## Use in a brand new application
If you're creating a new application, just clone <a href="https://github.com/bluenimble/bluenimble-apps-android-bootstrap" target="_blank"> bluenimble-apps-android-bootstrap</a>. It's a pre-configured android project that you can run immediately. *(Rename the app package id and name of the project)*

## Use in an existing application
If you're going to extend your existing application by adding new activities based on bluenimble sdk, add bluenimble-apps-sdk-android to your gradle build script.

**Step1:**
Add BlueNimble Apps SDK dependency on the gradle script:
```
compile 'com.github.bluenimble:bluenimble-apps-android-sdk:master-SNAPSHOT'
```

**Step2:**
Register one of the BlueNimble Application classes and the the UIActivity on your AndroidManifest.xml file:
```
<application
	.......
	android:name="com.bluenimble.apps.sdk.application.AssetsApplication">

	......

	<activity
		android:name="com.bluenimble.apps.sdk.application.UIActivity"
		android:configChanges="orientation|screenSize|keyboardHidden" />

</application>
```

What's a BlueNimble Application?
-----

A BlueNimble application is a folder under android assets which have the structure below:

- myApp
  - app.json
  - static.json (*optional*. Put application texts by user spoken language such as form fields labels - i18n)
  - backend.json (*optional*. Define your REST or local services)
  - themes (*optional*)
    - aTheme
    - anotherTheme
  - pages 
    - aPage.json
	- anotherPage.json
	- ...

You can also zip the myApp folder and tell the sdk to load the app from the archive. 
	
Visual Components - Out-Of-The-Box
-----

**bluenimble-apps** supports the most used and standard visual components
<table>
	<thead>
		<tr style="border: 0px;">
			<th>Id</th>
			<th>Description</th>
			<th>Declaration (on your pages, such as *page1.json*)</th>
		</tr>
	</thead>
	<tbody>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">text</td>
			<td style="border: 0px; padding-left: 20px;">Labels, titles and non editable description zones</td>
			<td style="border: 0px; padding-left: 20px;">`"text static.title ?"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">input</td>
			<td style="border: 0px; padding-left: 20px;">Editable input area</td>
			<td style="border: 0px; padding-left: 20px;">`"input:fullName ? ?"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">checkbox</td>
			<td style="border: 0px; padding-left: 20px;">Checkbox component (multi-choice)</td>
			<td style="border: 0px; padding-left: 20px;">`"checkbox:terms static.terms ?`</td>		
</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">radiogroup</td>
			<td style="border: 0px; padding-left: 20px;">RadioGroup component (single-choice)</td>
			<td style="border: 0px; padding-left: 20px;">`"radiogroup:gender static.gender ?"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">button</td>
			<td style="border: 0px; padding-left: 20px;">Button</td>
			<td style="border: 0px; padding-left: 20px;">`"button:create static.create ?"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">image</td>
			<td style="border: 0px; padding-left: 20px;">An image</td>
			<td style="border: 0px; padding-left: 20px;">`"image static.images.logo ?"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">dropdown</td>
			<td style="border: 0px; padding-left: 20px;">A single choice selectable list of values</td>
			<td style="border: 0px; padding-left: 20px;">`"dropdown:gender static.gender ?"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">list</td>
			<td style="border: 0px; padding-left: 20px;">A selectable list of values displayed as a list or grid</td>
			<td style="border: 0px; padding-left: 20px;">`"list:tasks static.tasks ? template=taskTpl layout=grid cols=3"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">/</td>
			<td style="border: 0px; padding-left: 20px;">Line break (could be appended after a component declaration)</td>
			<td style="border: 0px; padding-left: 20px;">`"/"` or `"text static.title ? /"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">map</td>
			<td style="border: 0px; padding-left: 20px;">Map component (based on google maps)</td>
			<td style="border: 0px; padding-left: 20px;">`"map:world ? ?"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">chart.line</td>
			<td style="border: 0px; padding-left: 20px;">Line Chart</td>
			<td style="border: 0px; padding-left: 20px;">`"chart.line static.charts.line ?"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">chart.bar</td>
			<td style="border: 0px; padding-left: 20px;">Bar Chart (horizontal and vertical)</td>
			<td style="border: 0px; padding-left: 20px;">`"chart.bar static.charts.bar ?"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">chart.pie</td>
			<td style="border: 0px; padding-left: 20px;">Pie Chart</td>
			<td style="border: 0px; padding-left: 20px;">`"chart.pie static.charts.pie ?"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">chart.bubble</td>
			<td style="border: 0px; padding-left: 20px;">Bubble Chart</td>
			<td style="border: 0px; padding-left: 20px;">`"chart.bubble static.charts.bubble ?"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">chart.radar</td>
			<td style="border: 0px; padding-left: 20px;">Radar Chart</td>
			<td style="border: 0px; padding-left: 20px;">`"chart.radar static.charts.radar ?"`</td>
		</tr>
		<tr style="border: 0px;">
			<td style="border: 0px; padding-left: 20px;">chart.scatter</td>
			<td style="border: 0px; padding-left: 20px;">Scatter Chart</td>
			<td style="border: 0px; padding-left: 20px;">`"chart.scatter static.charts.scatter ?"`</td>
		</tr>
	</tbody>
</table>


## Credits:
Thank you to the developers of the following libs, that BlueNimble Apps SDK uses underline:
<ul>
	<li>
		<a href="https://github.com/square/okhttp" target="_blank">OkHttp</a>
	</li>
	<li>
		<a href="https://github.com/square/picasso" target="_blank">Picasso</a>
	</li>
	<li>
		<a href="https://github.com/PhilJay/MPAndroidChart" target="_blank">MPAndroidChart</a>
	</li>
	<li>
		<a href="https://github.com/daimajia/AndroidViewAnimations" target="_blank">AndroidViewAnimations</a>
	</li>
	<li>
		<a href="http://mathparser.org/" target="_blank">MxParser</a>
	</li>
	<li>
		<a href="http://fontawesome.io/icons/" target="_blank">Font Awesome</a>
	</li>
</ul>

License
=======
Copyright 2016 BlueNimble, Inc.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
