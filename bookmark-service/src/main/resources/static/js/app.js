new Vue({
  el: '#app',
  data: {
    bookmarks: [],
    newBookmark: {},
    error: null
  },
  created: function () {
    //alert('page='+page)
    this.loadBookmarks(page);
  },
  methods: {
    loadBookmarks(page) {
      let self = this;
      $.getJSON("/api/v1/bookmarks?page="+page, function (data) {
        self.bookmarks = data
        self.error = null;
      }).fail(function (err){
        console.log("Error: ", err)
        self.error = "Failed to load bookmarks";
      });
    },

    saveBookmark() {
      let self = this;
      if(!this.newBookmark.url || !this.newBookmark.title) {
        alert("Please enter URL and Title");
        return;
      }
      $.ajax({
        type: "POST",
        url: '/api/v1/bookmarks',
        data: JSON.stringify(this.newBookmark),
        contentType: "application/json",
        success: function () {
          self.newBookmark = {};
          self.error = null;
          self.loadBookmarks(page);
        },
        error: function (err) {
            console.log("Error: ", err)
            self.error = "Failed to save bookmark";
        }
      });
    },
    upvoteBookmark(bookmarkId) {
      let self = this;
      console.log('up vote:' + bookmarkId);
      $.ajax({
        type: "PUT",
        url: '/api/v1/bookmarks/' + bookmarkId + '/votes/up',
        contentType: "application/json",
        success: function () {
          self.error = null;
          self.loadBookmarks(page);
        },
        error: function (jqXHR, exception) {
          console.log("Error: ", exception)
          self.error = "Failed to up vote bookmark";
        }
      });
    },
    downvoteBookmark(bookmarkId) {
      let self = this;
      console.log('down vote:' + bookmarkId);
      $.ajax({
        type: "PUT",
        url: '/api/v1/bookmarks/' + bookmarkId + '/votes/down',
        contentType: "application/json",
        success: function () {
          self.error = null;
          self.loadBookmarks(page);
        },
        error: function (jqXHR, exception) {
          console.log("Error: ", exception)
          self.error = "Failed to down vote bookmark";
        }
      });
    },
  }
});
