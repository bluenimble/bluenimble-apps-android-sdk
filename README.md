# BlueNimble Apps SDK for Android

**BlueNimble Apps SDK** lets you create android ***NATIVE*** applications **almost without writing a single line of code**. You can create complex pages, styling components, adding effects and integrate with your backend using simple json configuration files.

With almost no android experience, you can create advanced applications without the hassle of understanding how layouts, visual components, async tasks or backend integration works in android.

> **BlueNimble Apps SDK** sdk provides android developers with simple java interfaces which they can extend to create new themes, component types, effects, data binding, backend integration and more.

```
We would love to have an iOS developer join the initiative and create the iOS version of this SDK. 
If you are interested in implementing the specs, reach out at community@bluenimble.com!
```

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
  - app.json (basic declaration of the app, name, default theme, default language, entry page)
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
	
## Create a Page:

A page is basically a Json file representing a screen in your android app. Usually an application will have multiple pages through which users can navigate.
You can organize your application pages in folders under the directory `pages` if you want (by module names for e.g), or let them on the root of the `pages` folder.

e.g:
  - pages
    - home.json
	- help.json
	- authentication
	  - login.json
	  - signup.json
	- todo
	  - addTask.json
	  - listTasks.json

The components of a page are: ***Layers*** and ***Events***.


### Layers:
A layer is a section of a page, hosting visual components (buttons, labels etc..). It's main purpose is to organize your UI blocks (section of the page, popup dialog etc..).

A layer could have 3 states: 
- **Rendered:** by default. (visible)
- **Rendered and Hidden:** by adding the ***hidden*** keyword after the layout name. (invisible)
- **Not Rendered:** by adding the ***!*** character before the layout name. Useful in lists/grids. (not created therefore invisible)

e.g:
**myPage.json:** 
```
{
	"layers": {
		"header": [
			// components (buttons, images, labels etc...)
		],
		"main": [
			// components (buttons, images, labels etc...)
		],
		"hiddenBlock hidden": [
			// components (buttons, images, labels etc...)
		], 
		"! notRenderedBlock": [
			// components (buttons, images, labels etc...)
		]
	},
	"events": {
		// events go here
	}
}
```

As stated before, a layer is a group of: ***Components***.


### Components:
This is how to declare a component:

`"component:ID  bindingSET  bindingGET  property1=value1 property2=value2...  styleAttr1 styleAttr2... /"`

- **component:** the name of the component, *e.g: `text` for the label component*
- **ID:** *[optional]* useful when linking an event to the component or when binding data dynamically to/from it. (see events and binding sections)
- **bindingSet:** the value to be bound to this component, or the ***?*** if none. (see binding section)
- **bindingGet:** the value that this component will output, or the ***?*** if none. (see binding section)
- **properties:** *[optional]* predefined or custom properties of this component *e.g: `placeholder` for the input component*
- **styleAttributes:** *[optional]* keys identifying a specific style on the application theme. *e.g: `center` or `red`*
- **/ :** *[optional]* Break the line. (next element will be positioned below this one)

e.g:
**myPage.json:** 
```
{
	"layers": {
		"header": [
			"text  				static.icons.back 		?   icon",
			"image  			static.images.logo  	? 	logo"
		],
		"main": [
			"input:email 		? 						? 	placeholder=identity.email type=email center /",
			"dropdown:gender    static.identity.gender  ?   center /",
			"button:submit   	static.submit   		?   center"
		],
		"hiddenBlock hidden": [
			
		], 
		"! notRenderedBlock": [
		]
	},
	"events": {
		// events go here
	}
}
```

By default, the following is an exhaustive list of the current Out-Of-The-Box supported components:

**bluenimble-apps** supports the most used and standard visual components

| Id | Description | Spec Declaration |
| --- | --- | --- |
| text | Labels, titles and non editable description zones | `"text static.title ?"`|
| input | Editable input area | `"input:fullName ? ?"` |
| checkbox | Checkbox component (multi-choice) | `"checkbox:terms static.terms ?"` | 
| radiogroup | RadioGroup component (single-choice) | `"radiogroup:gender static.gender ?"` |
| button | Button | `"button:create static.create ?"` |
| image | An image | `"image static.images.logo ?"` |
| video | A basic video player | `"video:movie static.medias.videos.online ?"` |
| dropdown | A single choice selectable list of values | `"dropdown:gender static.gender ?"` |
| list | A selectable list of values displayed as a list or grid | `"list:tasks static.tasks ? template=taskTpl layout=grid cols=3"` |
| / | Line break (declared standalone on a line or appended to a component declaration) | `"/"` or `"text static.title ? /"` |
| map | Map component (based on google maps) | `"map:world ? ?"` |
| tabs | Swiping tabs | `"tabs static.articles.titles ? template=tabTpl1+tabTpl2+tabTpl3"` |
| chart.line | Line Chart | `"chart.line static.charts.line ?"` | 
| chart.bar | Bar Chart (horizontal and vertical) | `"chart.bar static.charts.bar ?"` |
| chart.pie | Pie Chart | `"chart.pie static.charts.pie ?"` |
| chart.bubble | Bubble Chart | `"chart.bubble static.charts.bubble ?"` |
| chart.radar | Radar Chart | `"chart.radar static.charts.radar ?"` |
| chart.scatter | Scatter Chart | `"chart.scatter static.charts.scatter ?"` |

*Refer to the <a href="">wiki page</a> on how you can easily create your own components.*


### Data Binding:
The underlying mechanism where data is temporarily stored is the **DataHolder**.
The DataHolder is an Object containing data for each of the following namespaces:
- **View:** Data coming from the UI components.
- **Streams:** Upload / Download files.
- **Error:** Error happened.
- **Device:** Information about the device such as Geolocation.
- **Static:** Data coming from the i18n file static.json.
- **Service names:** Data coming from service calls. (see Services section)

There are 2 types of Bindings.

#### Binding GET:
The Binding **GET** is the data retrieval process from a UI Component.

e.g: *Getting data from an input text field.*
`"input:fullName   ?   ?"`: the value of this field will be accessible (on your event for ex before an HTTP Call) from the dataHolder with `[view.fullName]`

To see the data of the DataHolder concerning all the views, simply use: `[view.*]`.

*We'll get into more details on the Events & Effects section*

#### Binding SET:
The Binding **SET** is the data putting process from a Data source into a UI Component.

e.g: *Putting data into an input text field.*
`"input:fullName   static.fullName   ?"`: this field will be populated by the value identified by the key `fullName` on the `static.json` file. (`static.json` being the data source in this example)


### Events & Effects:
Events can be user actions, such as clicking on a button, dragging a map or clicking on a layer or could be system occurrences, such as opening a new page.
**Events** have 4 callbacks from where ***effects*** will be called:
- **onStart:** Right before executing the action.
- **onSuccess:** The action associated with this event has been successful. 
- **onError:** The action associated with this event has failed / throwed an exception. 
- **onFinish:** Executed anyway, ***after*** onSuccess/onError.

Additionally, an event could run on a specific **Scope (layer)**, in order to restrict the data passed on the DataHolder to only be pulled from UI Components on that specific Scope.

**Effects** are operations affecting the UI, such as showing/hiding/removing a layer, styling, animating, navigating from a page to another one etc..

e.g:
**myPage.json:** 
```
{
	"layers": {
		"header": [
			"text  				static.icons.back 		?   icon",
			"image  			static.images.logo  	? 	logo"
		],
		"main": [
			"input:email 		? 						? 	placeholder=identity.email type=email center /",
			"dropdown:gender    static.identity.gender  ?   center /",
			"button:submit   	static.submit   		?   center"
		],
		"hiddenBlock hidden": [
			"text 				view.email 				?"
		], 
		"! notRenderedBlock": [
		]
	},
	"events": {
		"create": { // fired upon page creation
			"onStart": {
				"echo": "Page Creation." // Echo Effect
			}
		}, 
		"main.submit.press": {
			"scope": "main",
			"onStart": {
				"echo": "Selected: [view.*]", // Echo effect will display the content of the DataHolder of the main layer
				"hide": "main",  // the "main" layer will be hidden using the effect "hide".
				"show": "hiddenBlock" // the "hiddenBlock" layer previously hidden will be visible using the effect "show".
			}
		}
	}
}
```

Here is an exhaustive list of the current Out-of-the-box Events:

| Target | Events | 
| ------ | ------ | 
| text | <ul><li>`press`</li><li>`longPress`</li></ul> | 
| input | <ul><li>`afterTextChanged`</li><li>`beforeTextChanged`</li><li>`onTextChanged`</li></ul> | 
| checkbox | <ul><li>`select`</li></ul> | 
| radiogroup | <ul><li>`select`</li></ul> | 
| button | <ul><li>`press`</li><li>`longPress`</li></ul> | 
| image | <ul><li>`press`</li><li>`longPress`</li></ul> |
| dropdown | <ul><li>`select`</li></ul> | 
| list | <ul><li>`press`</li><li>`longPress`</li><li>Swipe (coming soon)</li><li>Scroll (coming soon)</li></ul> | 
| map | <ul><li>`move`</li><li>`press`</li><li>`longPress`</li><li>`markerDrag`</li><li>`markerStartDrag`</li><li>`markerEndDrag`</li><li>`markerPress`</li></ul> |
| layer | <ul><li>`press`</li><li>`longPress`</li></ul> | 
| page | <ul><li>`back`</li><li>`start`</li></ul> | 


Here is an exhaustive list of the current Out-of-the-box Effects:

| Effects | Purpose |
| ------- | ------- |
| hide |  |
| show |  |
| render |  |
| relocate |  |
| style |  |
| destroy |  |
| bind |  |
| unbind |  |
| goto |  |
| open |  |
| animate |  |
| select |  |
| clear |  |
| update |  |
| delete |  |
| play |  |
| resume |  |
| pause |  |
| seek |  |
| echo |  |


### Services:
Services represent communication with REST WebServices and phone Storage.
3 services are currently included out-of-the-box:
- **REST Calls** : GET, POST, PUT, DELETE.
- **SharedPreference** Operations (key-value android storage) : GET, POST, DELETE.
- **Local Storage** (Private and Public) : GET, POST, DELETE.

Services are declared on the `backend.json` file. The `data` serving as a payload to the service could be declared on the service declaration or on the event firing the call.

e.g:

```
{	
	"login": {
		"type": "remote",
		"url": "http://your-api/bnb-sdk-backend/api/login",
		"verb": "post",
		"headers": {
			"Content-Type": "application/json",
			"Accept": "application/json"
		},
		"data": {
			"username": "USERNAME",
			"password": "Python"
		}
	}
}
```

And, to call this service from your pages: 
**myPage.json:** 
```
{
	"layers": {
		"header": [
			"text  				static.icons.back 		?   icon",
			"image  			static.images.logo  	? 	logo"
		],
		"main": [
			"input:email 		? 						? 	placeholder=identity.email type=email center /",
			"dropdown:gender    static.identity.gender  ?   center /",
			"button:submit   	static.submit   		?   center", 
			"button:backend 	static.backend 			? 	right"
		],
		"hiddenBlock hidden": [
			"text 				view.email 				?"
		], 
		"! notRenderedBlock": [
		]
	},
	"events": {
		"create": { 
			"onStart": {
				"echo": "Page Creation." 
			}
		}, 
		"main.submit.press": {
			"scope": "main",
			"onStart": {
				"echo": "Selected: [view.*]", 
				"hide": "main", 
				"show": "hiddenBlock"
			}
		}, 
		"main.backend.press": {
			"call": { // Action to call the login service
				"services": "login"
			},
			"onSuccess": {
				"echo": "Success : UUID = [login.uuid]"
			},
			"onError": {
				"echo": "Error : [error.*]"
			},
			"onFinish": {
				"echo": "Finish : Response = [login.*]"
			}
		}
	}
}
```


### I18n Text resources - static.json:
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
And to display these strings on your components, you just need to append the *static* keyword to the key. 
*e.g :* 

`text static.title ?` will display a label component with `BlueNimble Apps SDK Browser` as value.

In other words, it will perform a `Binding SET` on the `text` component with as data source `static` and as value `static.title`.


## Styling:
You can define multiple themes for your application, one being the generic one and others for specific components and styles.

Developers themes should be placed under the `themes` directory of the app.

By default, Bluenimble SDK comes with a built-in default theme with basic styleAttributes such as `visible, hidden, center, rounded...`.

You can refer to your components from your theme, either:
- **By ID** : gives a specific style for this specific component ID, such as `logo` 
- **By Component type** : gives a generic style to all the components with the same type, such as `text`, which can be extended and overriden by other styleAttributes or by specifying an ID and refering it from the theme file.

You can refer to your theme from your components:
- **By Adding the style key on the end of the component declaration**, such as `text:title ? ? center`

The theme structure is pretty self-explanatory.

e.g: **theme1.json**

```
{
	"*": {
		"text": {
			"padding": "20 20 20 20",
			"text": {
				"align": "center middle",
				"color": "#FFFFFF",
				"shadow": "#7F000000 1 1 2"
			},
			"background": {
				"color": "#09afdf"
			}
		},
		"secondary": {
			"padding": "20 20 20 20",
			"text": {
				"align": "center middle",
				"color": "#FFFFFF",
				"shadow": "#7F000000 1 1 2"
			},
			"background": {
				"color": "#8bc43f"
			}
		},
		"logo": {
			"align": "middle",
			"margin": "? ? ? 20",
			"size": {
				"width": "500"
			}
		},
		"title": {
			"align": "right middle",
			"margin": "? 10 ? 10",
			"text": {
				"size": "14"
			}
		},
		"icon": {
			"size": {
				"width": "200",
				"height": "200"
			},
			"text": {
				"font": "fontawesome",
				"color": "#FFFFFF",
				"size": "24",
				"shadow": "#7F000000 1 1 2",
				"align": "center middle"
			},
			"background": {
				"color": "#FFFFFF #09afdf",
				"gradient": {
					"radius": "300"
				},
				"opacity": ".4"
			},
			"shadow": {
				"color": "#0293BD",
				"tick": "3"
			},
			"align": "center"
		},
		.....
	}
}
```


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
