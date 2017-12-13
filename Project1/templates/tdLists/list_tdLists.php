<section id="tasks">
  <h2>To Do Lists</h2>
  <ul>
      <div id="add">
          <li>
              <input type="text" placeholder="Create a list..." id="list">
              <button id="add2dl">
                  <img src="images/site/add.svg" alt="Add List">
                  <br>
              </button>
          </li>
      </div>
      <?php $counter = 0;
      foreach ($tdLists as $tdList) {$counter++ ?>
      <div id="remove">
          <li>
              <p href="index.php?tdl_id=<?=$tdList['tdl_id']?>"><?=$tdList['tdl_name']?></p>
              <button id="rem2dl">
                  <img src="images/site/delete.svg" alt="Remove List">
                  <br>
              </button>
          </li>
      </div>
      <?php } ?>

      <?php if (!$counter) { echo ("You don't have To Do Lists"); }?>
  </ul>
</section>

<script src="javascript/add_tdlist.js"></script>