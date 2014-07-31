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
	Android.TakePicture();
}
function getLoc()
{
	Android.GetCurrentLocation();
}
function submit()
{
	var name = document.getElementById('nameTextField').value;
	var loc = document.getElementById('locNameTextField').value;
	Android.Submit(name, loc)
}