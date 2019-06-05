
var path = require('path')
var fs = Promise.promisifyAll(require('fs-extra'));

exports.onCreateProject = function (api, app, config, cb) {
  var app_path = app.paths.root;
  if (config.target == 'native-ios') {
    return new Promise.resolve(nil)
  } else if (config.target == 'native-android') {
    // copy google-services.json from manifest config string `google_services_file`
    var googleServicesJsonFile = path.join(app_path, app.manifest.android.google_services_file);
    fs.copySync(googleServicesJsonFile,
    path.join(config.outputPath, app.manifest.shortName, "app", "google-services.json"));
    return Promise.resolve(true);
  }

}
