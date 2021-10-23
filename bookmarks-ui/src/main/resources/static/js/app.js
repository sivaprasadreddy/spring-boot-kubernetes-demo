new Vue({
  el: '#app',
  data: {
    bookmarks: [],
    newBookmark: { url:'', title:''},
    error: null
  },
  created: function () {
    this.loadBookmarks();
  },
  methods: {
    loadBookmarks() {
      let self = this;
      $.getJSON(apiBaseUrl+"/bookmarks/api/v1/bookmarks", function (data) {
        self.bookmarks = data
        self.error = null;
      }).fail(function (err){
        console.log("Error: ", err)
        self.error = "Failed to load bookmarks";
      });
    },
    urlChangeHandler() {
      let self = this;
      console.log(self.newBookmark)
      if(self.newBookmark.title !== undefined && self.newBookmark.title !== '') {
        return;
      }
      $.ajax({
        type: "GET",
        url: apiBaseUrl+'/url-metadata/api/v1/url-metadata?url='+self.newBookmark.url,
        success: function (response) {
          console.log("urlChangeHandler:", response);
          self.newBookmark.title= response.title;
        },
        error: function (err) {
          console.log("Error: ", err)
        }
      });
    },
    saveBookmark() {
      let self = this;
      if(self.newBookmark.url === '') {
        self.error = "Please enter URL";
        return;
      }
      $.ajax({
        type: "POST",
        url: apiBaseUrl+'/bookmarks/api/v1/bookmarks',
        data: JSON.stringify(this.newBookmark),
        contentType: "application/json",
        success: function () {
          self.newBookmark = {};
          self.error = null;
          self.loadBookmarks();
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
        url: apiBaseUrl+'/bookmarks/api/v1/bookmarks/' + bookmarkId + '/votes/up',
        contentType: "application/json",
        success: function () {
          self.error = null;
          self.loadBookmarks();
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
        url: apiBaseUrl+'/bookmarks/api/v1/bookmarks/' + bookmarkId + '/votes/down',
        contentType: "application/json",
        success: function () {
          self.error = null;
          self.loadBookmarks();
        },
        error: function (jqXHR, exception) {
          console.log("Error: ", exception)
          self.error = "Failed to down vote bookmark";
        }
      });
    },
  }
});
