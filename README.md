# BlueNimble Apps SDK for Android

**bluenimble-apps** is an sdk let you create android applications by creating pages, styling components, adding effects and integrate with your backend using simple json configuration files.

With almost no android experience, you can create advanced applications without the hassle of understanding how layouts, visual components, asyc tasks or backend integration works in android.

**bluenimble-apps** sdk provides android developers with simple java interfaces which they can extend to create new themes, component types, effects, data binding, backend integration and more

## Getting Started

### Use in a brand new application
If you're creating a new application, just clone bluenimble-apps-android-bootstrap. It's a pre-configured android project that you can run immediately.

### Use in an existing application
If you're going to extend your existing application by adding new activities based on bluenimble sdk, add bluenimble-apps-sdk-android to your gradle build script

## What's a BlueNimble Application 

A BlueNimble application is a folder under android assets which have the structure bellow:

- myApp
  - app.json
  - themes (optional)
    - aTheme
  - pages 
    - aPage.json
	- anotherPage.json
	- ...

## Visual Components - Out-Of-The-Box

**bluenimble-apps** supports out-of-the-box the most used and standard visual components
<table>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
text
</td>
<td style="border: 0px; padding-left: 20px;">
Labels, titles and non editable description zones
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
input
</td>
<td style="border: 0px; padding-left: 20px;">
Create editable input areas
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
checkbox
</td>
<td style="border: 0px; padding-left: 20px;">
A Checkbox component
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
radiogroup
</td>
<td style="border: 0px; padding-left: 20px;">
A RadioGroup component
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
button
</td>
<td style="border: 0px; padding-left: 20px;">
A button 
</td>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
image
</td>
<td style="border: 0px; padding-left: 20px;">
An image 
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
dropdown
</td>
<td style="border: 0px; padding-left: 20px;">
a selectable list of values 
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
list
</td>
<td style="border: 0px; padding-left: 20px;">
a selectable list of values 
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
map
</td>
<td style="border: 0px; padding-left: 20px;">
map component 
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
chart.line
</td>
<td style="border: 0px; padding-left: 20px;">
Line Chart 
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
chart.bar
</td>
<td style="border: 0px; padding-left: 20px;">
Bar Chart 
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
chart.pie
</td>
<td style="border: 0px; padding-left: 20px;">
Pie Chart 
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
chart.bubble
</td>
<td style="border: 0px; padding-left: 20px;">
Bubble Chart 
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
chart.radar
</td>
<td style="border: 0px; padding-left: 20px;">
Radar Chart 
</td>
</tr>
<tr style="border: 0px;">
<td style="border: 0px; padding-left: 20px;">
chart.scatter
</td>
<td style="border: 0px; padding-left: 20px;">
Scatter Chart 
</td>
</tr>
</table>
