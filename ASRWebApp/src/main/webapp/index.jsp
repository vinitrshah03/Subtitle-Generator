<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8" isELIgnored="true"%>

<!DOCTYPE html>

<html>
<head>
<title>Speech to Subtitles</title>

<style>

/* NAVBAR */
.navbar{
background:#007BFF;
color:white;
padding:15px;
font-size:20px;
font-weight:bold;
text-align:center;
}

/* BODY */
body{
font-family: Arial;
margin:0;
background:#f5f5f5;
}

.container{
margin:40px;
background:white;
padding:20px;
border-radius:8px;
box-shadow:0 2px 10px rgba(0,0,0,0.1);
}

h2{
color:#333;
}

button{
padding:10px 20px;
background:#007BFF;
border:none;
color:white;
border-radius:5px;
cursor:pointer;
margin:5px;
}

button:hover{
background:#0056b3;
}

table{
width:100%;
border-collapse:collapse;
margin-top:20px;
}

th,td{
border:1px solid #ddd;
padding:8px;
text-align:left;
}

th{
background:#007BFF;
color:white;
}

.download{
margin-top:20px;
}

/* Toggle buttons */
.toggle-btn{
background:#6c757d;
}

.active{
background:#28a745 !important;
}

</style>

</head>

<body>

<!-- NAVBAR -->

<div class="navbar">
AI Subtitle Generator
</div>

<div class="container">

<h2>Upload Audio / Video File</h2>

<form id="uploadForm">

<input type="file" name="mediafile" required>

<br><br>

<label>Source Language:</label>

<select name="source_lang">
<option value="hi">Hindi</option>
<option value="en">English</option>
<option value="ta">Tamil</option>
<option value="gu">Gujarati</option>
</select>

<br><br>

<button type="submit">Generate Subtitles</button>

</form>

<div id="loading" style="display:none;margin-top:20px;">
Processing audio... please wait...
</div>

<!-- Toggle Buttons -->

<div style="margin-top:20px; display:none;" id="toggleSection">
<button id="showEnglish" class="active">English Subtitles</button>
<button id="showOriginal" class="toggle-btn">Original Subtitles</button>
</div>

<!-- English Subtitles Table -->

<table id="englishTable" style="display:none;">
<thead>
<tr>
<th>#</th>
<th>Start</th>
<th>End</th>
<th>Subtitle (English)</th>
</tr>
</thead>
<tbody></tbody>
</table>

<!-- Original Subtitles Table -->

<table id="originalTable" style="display:none;">
<thead>
<tr>
<th>#</th>
<th>Start</th>
<th>End</th>
<th>Subtitle (Original)</th>
</tr>
</thead>
<tbody></tbody>
</table>

<button class="download" id="downloadBtn" style="display:none;">
Download SRT
</button>

</div>

<script>

let subtitlesData = []
let currentView = "english"

/* Form Submit */

document.getElementById("uploadForm").addEventListener("submit", async function(e){

e.preventDefault()

document.getElementById("loading").style.display="block"

let formData = new FormData(this)

let res = await fetch("ASRClient", {
method:"POST",
body:formData
})

let data = await res.json()

document.getElementById("loading").style.display="none"

subtitlesData = data.subtitles

renderTables(subtitlesData)

})

/* Render Tables */

function renderTables(subs){

let engBody = document.querySelector("#englishTable tbody")
let orgBody = document.querySelector("#originalTable tbody")

engBody.innerHTML=""
orgBody.innerHTML=""

subs.forEach((s,i)=>{

let engRow = `
<tr>
<td>${i+1}</td>
<td>${formatTime(s.start)}</td>
<td>${formatTime(s.end)}</td>
<td>${s.translated_text || s.text}</td>
</tr>
`

let orgRow = `
<tr>
<td>${i+1}</td>
<td>${formatTime(s.start)}</td>
<td>${formatTime(s.end)}</td>
<td>${s.text}</td>
</tr>
`

engBody.innerHTML += engRow
orgBody.innerHTML += orgRow

})

document.getElementById("toggleSection").style.display="block"
document.getElementById("englishTable").style.display="table"
document.getElementById("originalTable").style.display="none"
document.getElementById("downloadBtn").style.display="inline-block"

}

/* Toggle Logic */

document.getElementById("showEnglish").onclick = () => {
	
currentView = "english"

document.getElementById("englishTable").style.display="table"
document.getElementById("originalTable").style.display="none"

document.getElementById("showEnglish").classList.add("active")
document.getElementById("showOriginal").classList.remove("active")

}

document.getElementById("showOriginal").onclick = () => {
	
currentView = "original"

document.getElementById("englishTable").style.display="none"
document.getElementById("originalTable").style.display="table"

document.getElementById("showOriginal").classList.add("active")
document.getElementById("showEnglish").classList.remove("active")

}

/* Time Format */

function formatTime(sec){

let date = new Date(sec*1000)

let h = String(date.getUTCHours()).padStart(2,"0")
let m = String(date.getUTCMinutes()).padStart(2,"0")
let s = String(date.getUTCSeconds()).padStart(2,"0")
let ms = String(date.getUTCMilliseconds()).padStart(3,"0")

return `${h}:${m}:${s},${ms}`

}

/* Download SRT */

document.getElementById("downloadBtn").addEventListener("click",()=>{

let srt=""

subtitlesData.forEach((s,i)=>{

srt += `${i+1}\n`
srt += `${formatTime(s.start)} --> ${formatTime(s.end)}\n`

let textToUse = ""

if (currentView === "english") {
    textToUse = s.translated_text || s.text
} else {
    textToUse = s.text
}

srt += `${textToUse}\n\n`

})

let blob = new Blob([srt],{type:"text/plain"})
let url = URL.createObjectURL(blob)

let a=document.createElement("a")
a.href=url
a.download = currentView === "english"
    ? "subtitles_english.srt"
    : "subtitles_original.srt"

a.click()

})
</script>

</body>
</html>
