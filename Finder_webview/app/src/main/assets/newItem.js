/**
 * Created by:  Evan on 7/29/2014
 * Last Edited: 7/29/2014
 * Project:     Finder
 * Package:     assets
 * File:        newItem.js
 * Purpose:     Methods that provide functionality to the HTML elements. Communicates with the Java code.
 */

function takePicture()
{
	var name = document.getElementById('nameText').value;
	var loc = document.getElementById('locationNameText').value;
	Android.TakePicture(name, loc);
}
function getLoc()
{
	Android.GetCurrentLocation();
}
function submit()
{
	var name = document.getElementById('nameText').value;
	var loc = document.getElementById('locationNameText').value;
	Android.Submit(name, loc)
}
