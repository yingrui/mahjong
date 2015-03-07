basePath = '../';

frameworks = ["jasmine"];

files = [
  JASMINE,
  JASMINE_ADAPTER,
  'app/lib/jquery-1.8.2.js',
  'app/lib/underscore-1.4.2.js',
  'app/lib/angular/angular.js',
  'app/js/**/*.js',
  'test/lib/angular/angular-mocks.js',
  'test/unit/**/*.js'
];

autoWatch = false;

browsers = ['Chrome'];

singleRun = false;
