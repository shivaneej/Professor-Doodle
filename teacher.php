<!DOCTYPE html>
<html>
<head>
  <title>PDoodle</title>
  <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
  <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
  <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
  <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">

  <style type="text/css">
    .card-body
    {
      margin: auto;
    }
    .material-icons
    {
      font-size: 100px;
      color: #898989;
    }
    .mr-auto, .mx-auto 
    {
        margin-left: 93%;
    }
  </style>
</head>
<body>


  <nav class="navbar navbar-expand-lg navbar-light bg-light navbar-right">
  <a class="navbar-brand" href="#">PDoodle</a>
  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
    <span class="navbar-toggler-icon"></span>
  </button>

  <div class="collapse navbar-collapse" id="navbarSupportedContent">
    <ul class="navbar-nav mr-auto">
      <!-- <button type="button" class="btn btn-light">Logout</button> -->
      <form action="#" method="post">
        <input type="submit" name="logout_link" class="btn btn-light" value="Logout" id="logout">
      </form>
        <?php



        if(isset($_POST['logout_link']))
        {
          session_start();
          session_destroy();
          header("Location: student.php");
        }
              ?> 
    </ul>

  </div>
</nav>


<div class="col-md-4"></div>
<div class="card col-md-4">
  <div class="card-body">
    <i class="material-icons add">add_circle_outline</i>
    <p class="card-text">Have a doubt?</p>
    <a href="#" class="btn btn-primary" data-toggle="modal" data-target="#requestModal">Request Teacher </a>
  </div>
</div>

<div class="modal fade" id="requestModal">
  <div class="modal-dialog modal-dialog-centered" role="document">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="requestModalLabel">Create New Request</h5>
        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
          <span aria-hidden="true">&times;</span>
        </button>
      </div>
      <div class="modal-body">
        <form method="POST">
          <div class="form-group">
            <label for="title">Topic <span style="color: red;">*</span></label>
            <input class="form-control" type="text" name="reqTitle" placeholder="Enter Topic" required>
          </div>
          <div class="form-group">
            <label for="desc">Description</label>
          <textarea class="form-control" id="desc" name="reqDesc" placeholder="Enter Description (Optional)"></textarea>
        </div>
        <div class="form-group">
          <label for="teacher">Select Teacher <span style="color: red;">*</span></label>
          <select class="form-control" id="teacher" name="teacher">
            <option>--Select Teacher--</option>


            <?php

              require 'vendor/autoload.php';
              $collection = (new MongoDB\Client)->newlol->teacher;
              $cursor = $collection->find();
              foreach ($cursor as $document){
                ?><option><?php echo $document["First Name"]; ?> - <?php echo $document["Subject"]; ?></option><?php
              }
            ?>
          </select>
        </div>
        </form>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary">Submit Request</button>
      </div>
    </div>
  </div>
</div>

</body>
</html>