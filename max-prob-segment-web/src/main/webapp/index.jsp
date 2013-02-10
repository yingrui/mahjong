<%@ page pageEncoding="UTF-8"%>
<!doctype html>

<html>
<head>
  <meta content-type="text/html; charset=utf-8">
  <title>Max Probability Segment - an Efficient Chinese Breaking Algorithm</title>

  <meta content="IE=edge,chrome=1" http-equiv="X-UA-Compatible">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">

  <link href="http://twitter.github.com/bootstrap/assets/css/bootstrap.css" rel="stylesheet">
  <link href="http://twitter.github.com/bootstrap/assets/css/bootstrap-responsive.css" rel="stylesheet">
  <link rel="stylesheet" href="http://twitter.github.com/bootstrap/assets/js/google-code-prettify/prettify.css">

  <!--
  IMPORTANT:
  This is Heroku specific styling. Remove to customize.
  -->
  <link href="http://heroku.github.com/template-app-bootstrap/heroku.css" rel="stylesheet">
  <style type="text/css">
    .instructions {
      display: none;
    }

    .instructions li {
      margin-bottom: 10px;
    }

    .instructions h2 {
      margin: 18px 0;
    }

    .instructions blockquote {
      margin-top: 10px;
    }

    .screenshot {
      margin-top: 10px;
      display: block;
    }

    .screenshot a {
      padding: 0;
      line-height: 1;
      display: inline-block;
      text-decoration: none;
    }

    .screenshot img, .tool-choice img {
      border: 1px solid #ddd;
      -webkit-border-radius: 4px;
      -moz-border-radius: 4px;
      border-radius: 4px;
      -webkit-box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075);
      -moz-box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075);
      box-shadow: 0 1px 1px rgba(0, 0, 0, 0.075);
    }
  </style>
  <!-- /// -->
  <script type="text/javascript">
    <!--
    function appname() {
      return location.hostname.substring(0, location.hostname.indexOf("."));
    }
    // -->
  </script>
</head>

<body>
<div class="navbar navbar-fixed-top">
  <div class="navbar-inner">
    <div class="container">
      <a href="/" class="brand">Websiteschema max-prob-segment-web</a>
    </div>
  </div>
</div>

<div class="container" id="getting-started">
<div class="row">
<div class="span8 offset2">
<h1 class="alert alert-success">乒乓球拍卖完了...</h1>

<div class="page-header">
  <h1>Max Probability Segment</h1>
</div>

<div style="margin-bottom: 20px">
  <p><b>Max probability segment</b> is a simple and efficent algorithm for Chinese Breaking.</p>
  <p>max-prob-segment is the Java implementation of this algorithm.
  By now it not only can break Chinese words, it also support Part-Of-Speech, Chinese Pinyin Recognition, Chinese Concept Tree etc.
  It is an open source software, and looking forward your contributions.</p>
  <p>You could try it by click <a href="/app/index.htm">here</a>, we also provide a Web Api if you just use Chinese Breaking rarely.</p>
  <h3>Segment Web Api</h3>
  <p><b>max-prob-segment-web</b> offer a Web Api: <a href="http://max-prob-segment.herokuapp.com/api/segment?sentence=input+word+here">http://max-prob-segment.herokuapp.com/api/segment</a></p>
  <p>Which receive both GET and POST method; please do not forget to add header content-type:"application/json;charset=utf-8" and header accept:"application/json"</p>
  <p>GET /api/segment?sentence={your words here}</p>
  <p>POST /api/segment?params...\n\n<br/>
     {your words here in request body}
  </p>
</div>


<!-- end tab content -->
</div>
</div>
</div>


<script src="http://twitter.github.com/bootstrap/assets/js/jquery.js"></script>
<script src="http://twitter.github.com/bootstrap/assets/js/bootstrap-modal.js"></script>
<script src="http://twitter.github.com/bootstrap/assets/js/bootstrap-tab.js"></script>
</body>
</html>
