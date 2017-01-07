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

| Id | Description | Spec Declaration |
| --- | --- | --- |
| text | Labels, titles and non editable description zones | `"text static.title ?"`|
| input | Editable input area | `"input:fullName ? ?"` |
| checkbox | Checkbox component (multi-choice) | `"checkbox:terms static.terms ?"` | 
| radiogroup | RadioGroup component (single-choice) | `"radiogroup:gender static.gender ?"` |
| button | Button | `"button:create static.create ?"` |
| image | An image | `"image static.images.logo ?"` |
| dropdown | A single choice selectable list of values | `"dropdown:gender static.gender ?"` |
| list | A selectable list of values displayed as a list or grid | `"list:tasks static.tasks ? template=taskTpl layout=grid cols=3"` |
| / | Line break (declared standalone or appended to a component declaration) | `"/"` or `"text static.title ? /"` |
| map | Map component (based on google maps) | `"map:world ? ?"` |
| chart.line | Line Chart | `"chart.line static.charts.line ?"` | 
| chart.bar | Bar Chart (horizontal and vertical) | `"chart.bar static.charts.bar ?"` |
| chart.pie | Pie Chart | `"chart.pie static.charts.pie ?"` |
| chart.bubble | Bubble Chart | `"chart.bubble static.charts.bubble ?"` |
| chart.radar | Radar Chart | `"chart.radar static.charts.radar ?"` |
| chart.scatter | Scatter Chart | `"chart.scatter static.charts.scatter ?"` |

I18n Text resources - static.json
-----
This is where you put your text resources that will be displayed on your application. There are multiple ways of organizing your strings depending on the component you are displaying it with.
```
{	
	"title": {
		"en": "BlueNimble Apps SDK Browser", 
		"fr": "BlueNimble Apps SDK Navigateur"
	},
	"terms": {
		"en": "Terms and conditions"
	}, 
	"gender": {
		"en": [
			"Male",
			"Female",
			"Other"
		]
	},
	"company": {
		"name": {
			"en": "BlueNimble"
		}, 
		"positions": {
			"en": [
				{
					"id": "pos0", 
					"value": "Developer"
				}, 
				{
					"id": "pos1", 
					"value": "Architect"
				}, 
				{
					"id": "pos2", 
					"value": "Sys Admin"
				}
			]
		}
	}
}
```
And to display these strings on your components, you just need to append the *static* keyword to your their keys. 
*e.g :* `text static.title ?` will display a label component with `BlueNimble Apps SDK Browser` as value.

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
