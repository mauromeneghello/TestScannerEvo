import core.RepoCloner;
import instrumentor.JSASTInstrumentor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.*;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;

import core.JSAnalyzer;
import core.TraceAnalyzer;
import core.RepoCloner;

public class GetRepoStat {

	private static String[] repoList = {
		/*"https://github.com/gabrielecirulli/2048",
		"https://github.com/ajaxorg/ace",
		"https://github.com/doublespeakgames/adarkroom",
		"https://github.com/fabien-d/alertify.js/tree/0.3",
		"https://github.com/angular-app/angular-app",
		"https://github.com/angular/angular.js",
		"https://github.com/samizdatco/arbor",
		"https://github.com/caolan/async/",
		"https://github.com/atom/atom",
		"https://github.com/aws/aws-sdk-js",
		"https://github.com/babel/babel",
		"https://github.com/BabylonJS/Babylon.js",
		"https://github.com/jashkenas/backbone",
		"https://github.com/marionettejs/backbone.marionette",
		"https://github.com/yaronn/blessed-contrib",
		"https://github.com/petkaantonov/bluebird",
		"https://github.com/expressjs/body-parser",*/
		"https://github.com/angular-ui/bootstrap",/*
		"https://github.com/eternicode/bootstrap-datepicker",
		"https://github.com/dangrossman/bootstrap-daterangepicker/",
		"https://github.com/jschr/bootstrap-modal/",
		"https://github.com/silviomoreto/bootstrap-select",
		"https://github.com/jhollingworth/bootstrap-wysihtml5",
		"https://github.com/twbs/bootstrap",
		"https://github.com/bower/bower",
		"https://github.com/adobe/brackets",
		"https://github.com/harthur/brain",
		"https://github.com/mozbrick/brick",
		"https://github.com/nodejitsu/browsenpm.org",
		"https://github.com/BrowserSync/browser-sync",
		"https://github.com/mozilla/BrowserQuest",
		"https://github.com/alexwolfe/Buttons/",
		"https://github.com/masayuki0812/c3",
		"https://github.com/jessepollak/card",
		"https://github.com/n1k0/casperjs",
		"https://github.com/AnalyticalGraphicsInc/cesium",
		"https://github.com/sindresorhus/chalk",
		"https://github.com/nnnick/Chart.js",
		"https://github.com/gionkunz/chartist-js",
		"https://github.com/MatthewMueller/cheerio",
		"https://github.com/GoalSmashers/clean-css",
		"https://github.com/medikoo/cli-color",
		"https://github.com/ellisonleao/clumsy-bird",
		"https://github.com/NeXTs/Clusterize.js",
		"https://github.com/cocos2d/cocos2d-html5",
		"https://github.com/codemirror/codemirror",
		"https://github.com/jashkenas/coffeescript",
		"https://github.com/jackmoore/colorbox",
		"https://github.com/Marak/colors.js",
		"https://github.com/tj/commander.js",
		"https://github.com/componentjs/component",
		"http://github.com/maxogden/concat-stream/",
		"https://github.com/senchalabs/connect",
		"https://github.com/karpathy/convnetjs",
		"https://github.com/craftyjs/Crafty",
		"https://github.com/square/crossfilter",
		"https://github.com/square/cubism",
		"https://github.com/piqnt/cutjs",
		"https://github.com/mbostock/d3",
		"https://github.com/keen/dashboards",
		"https://github.com/Shopify/dashing",
		"https://github.com/alanshaw/david-www",
		"https://github.com/dc-js/dc.js",
		"https://github.com/visionmedia/debug",
		"https://github.com/imakewebthings/deck.js",
		"https://github.com/joemccann/dillinger",
		"https://github.com/bevacqua/dragula",
		"https://github.com/enyo/dropzone/",
		"https://github.com/michaelvillar/dynamics.js",
		"https://github.com/CreateJS/EaselJS",
		"https://github.com/ecomfe/echarts",
		"https://github.com/visionmedia/ejs",
		"https://github.com/emberjs/ember.js",
		"https://github.com/playcanvas/engine",
		"https://github.com/HumbleSoftware/envisionjs",
		"https://github.com/OscarGodson/EpicEditor",
		"https://github.com/es-shims/es5-shim",
		"https://github.com/Constellation/escodegen/",
		"https://github.com/ether/etherpad-lite",
		"https://github.com/strongloop/express",
		"https://github.com/kangax/fabric.js",
		"https://github.com/Marak/faker.js",
		"https://github.com/Netflix/falcor",
		"https://github.com/ftlabs/fastclick",
		"https://github.com/github/fetch",
		"https://github.com/FineUploader/fine-uploader",
		"https://github.com/davatron5000/FitVids.js",
		"https://github.com/designmodo/Flat-UI",
		"https://github.com/woothemes/FlexSlider",
		"https://github.com/flightjs/flight",
		"https://github.com/flot/flot",
		"https://github.com/facebook/flux",
		"https://github.com/foreverjs/forever",
		"https://github.com/malsup/form/",
		"https://github.com/zurb/foundation-sites",
		"https://github.com/aurelia/framework",
		"https://github.com/ExactTarget/fuelux",
		"https://github.com/fullcalendar/fullcalendar",
		"https://github.com/alvarotrigo/fullPage.js",
		"https://github.com/yeoman/generator",
		"https://github.com/TryGhost/Ghost",
		"https://github.com/hpneo/gmaps",
		"https://github.com/grafana/grafana",
		"https://github.com/greensock/GreenSock-JS",
		"https://github.com/marmelab/gremlins.js",
		"https://github.com/ducksboard/gridster.js/",
		"https://github.com/gruntjs/grunt",
		"https://github.com/gulpjs/gulp",
		"https://github.com/wearefractal/gulp-util/",
		"https://github.com/hacksalot/HackMyResume",
		"https://github.com/hammerjs/hammer.js",
		"https://github.com/wycats/handlebars.js/",
		"https://github.com/handsontable/handsontable",
		"https://github.com/hexojs/hexo/",
		"https://github.com/Hextris/hextris",
		"https://github.com/highcharts/highcharts",
		"https://github.com/isagalaev/highlight.js",
		"https://github.com/redis/hiredis-node",
		"https://github.com/twitter/hogan.js/",
		"https://github.com/imsky/holder",
		"https://github.com/goldfire/howler.js/",
		"https://github.com/niklasvh/html2canvas",
		"https://github.com/h5bp/html5-boilerplate",
		"https://github.com/ashtuchkin/iconv-lite",
		"https://github.com/codio/iloveopensource",
		"https://github.com/facebook/immutable-js",
		"https://github.com/impress/impress.js",
		"https://github.com/infinite-scroll/infinite-scroll",
		"https://github.com/SBoudrias/Inquirer.js",
		"https://github.com/taye/interact.js",
		"https://github.com/usablica/intro.js/",
		"https://github.com/driftyco/ionic",
		"https://github.com/cubiq/iscroll/",
		"https://github.com/metafizzy/isotope",
		"https://github.com/gotwarlost/istanbul",
		"https://github.com/visionmedia/jade",
		"https://github.com/jasmine/jasmine",
		"https://github.com/jquery/jquery",
		"https://github.com/tuupola/jquery_lazyload",
		"https://github.com/blueimp/jQuery-File-Upload",
		"https://github.com/aterrien/jQuery-Knob",
		"https://github.com/kamens/jQuery-menu-aim",
		"https://github.com/jquery/jquery-mobile",
		"https://github.com/defunkt/jquery-pjax",
		"https://github.com/mathiasbynens/jquery-placeholder",
		"https://github.com/jquery/jquery-ui",
		"https://github.com/jzaefferer/jquery-validation",
		"https://github.com/briangonzalez/jquery.adaptive-backgrounds.js",
		"https://github.com/nodeca/js-yaml",
		"https://github.com/jsdoc3/jsdoc",
		"https://github.com/tmpvar/jsdom",
		"https://github.com/jshint/jshint",
		"https://github.com/douglascrockford/JSON-js",
		"https://github.com/typicode/json-server",
		"https://github.com/MrRio/jsPDF",
		"https://github.com/VerbalExpressions/JSVerbalExpressions",
		"https://github.com/karma-runner/karma",
		"https://github.com/kartograph/kartograph.js",
		"https://github.com/madrobby/keymaster/",
		"https://github.com/keystonejs/keystone",
		"https://github.com/docker/kitematic",
		"https://github.com/gamelab/kiwi.js",
		"https://github.com/prawnsalad/KiwiIRC",
		"https://github.com/knockout/knockout",
		"https://github.com/loadfive/Knwl.js",
		"https://github.com/koajs/koa",
		"https://github.com/aFarkas/lazysizes",
		"https://github.com/Leaflet/Leaflet",
		"https://github.com/less/less.js",
		"https://github.com/sdelements/lets-chat",
		"https://github.com/davatron5000/Lettering.js",
		"https://github.com/javve/list.js/",
		"https://github.com/emgram769/live4chan",
		"https://github.com/mozilla/localForage",
		"https://github.com/lodash/lodash",
		"https://github.com/nomiddlename/log4js-node",
		"https://github.com/strongloop/loopback",
		"https://github.com/google/lovefield",
		"https://github.com/LazerUnicorns/lycheeJS",
		"https://github.com/dimsemenov/Magnific-Popup",
		"https://github.com/adam-p/markdown-here/",
		"https://github.com/evilstreak/markdown-js",
		"https://github.com/chjj/marked",
		"https://github.com/chipersoft/mash.li",
		"https://github.com/desandro/masonry",
		"https://github.com/angular/material",
		"https://github.com/callemall/material-ui",
		"https://github.com/linnovate/mean",
		"https://github.com/jansmolders86/mediacenterjs",
		"https://github.com/yabwe/medium-editor",
		"https://github.com/melonjs/melonJS",
		"https://github.com/knsv/mermaid",
		"https://github.com/meteor/meteor",
		"https://github.com/mozilla/metrics-graphics/",
		"https://github.com/olton/Metro-UI-CSS/",
		"https://github.com/isaacs/minimatch/",
		"https://github.com/substack/minimist",
		"https://github.com/lhorie/mithril.js",
		"https://github.com/mochajs/mocha",
		"https://github.com/Modernizr/Modernizr",
		"https://github.com/stamen/modestmaps-js",
		"https://github.com/moment/moment",
		"https://github.com/Automattic/mongoose",
		"https://github.com/morrisjs/morris.js/",
		"https://github.com/ccampbell/mousetrap",
		"https://github.com/janl/mustache.js",
		"https://github.com/nylas/n1/",
		"https://github.com/flatiron/nconf/",
		"https://github.com/AvianFlu/ncp",
		"https://github.com/npm/newww",
		"https://github.com/ngbp/ngbp",
		"https://github.com/segmentio/nightmare",
		"https://github.com/nodejs/node",
		"https://github.com/noderedis/node_redis/",
		"https://github.com/substack/node-browserify",
		"https://github.com/justmoon/node-extend",
		"https://github.com/jprichardson/node-fs-extra",
		"https://github.com/isaacs/node-glob/",
		"https://github.com/isaacs/node-graceful-fs",
		"https://github.com/nodejitsu/node-http-proxy",
		"https://github.com/node-inspector/node-inspector",
		"https://github.com/jscs-dev/node-jscs",
		"https://github.com/broofa/node-mime/",
		"https://github.com/substack/node-mkdirp",
		"https://github.com/felixge/node-mysql",
		"https://github.com/ciaranj/node-oauth/",
		"https://github.com/pwnall/node-open",
		"https://github.com/substack/node-optimist",
		"https://github.com/isaacs/node-semver",
		"https://github.com/shtylman/node-uuid",
		"https://github.com/NodeBB/NodeBB",
		"https://github.com/nodemailer/nodemailer",
		"https://github.com/harthur/nomnom/",
		"https://github.com/isaacs/nopt/",
		"https://github.com/jaredreich/notie.js",
		"https://github.com/needim/noty",
		"https://github.com/npm/npm",
		"https://github.com/npm/npm-www",
		"https://github.com/rstacruz/nprogress",
		"https://github.com/novus/nvd3",
		"https://github.com/jimhigson/oboe.js",
		"https://github.com/peachananr/onepage-scroll",
		"https://github.com/ericf/open-marriage",
		"https://github.com/sindresorhus/pageres",
		"https://github.com/paperjs/paper.js",
		"https://github.com/ParsePlatform/parse-server",
		"https://github.com/guillaumepotier/Parsley.js",
		"https://github.com/VincentGarreau/particles.js",
		"https://github.com/jaredhanson/passport",
		"https://github.com/qiao/PathFinding.js",
		"https://github.com/mozilla/pdf.js",
		"https://github.com/benpickles/peity",
		"https://github.com/pencilblue/pencilblue",
		"https://github.com/photonstorm/phaser",
		"https://github.com/dimsemenov/PhotoSwipe",
		"https://github.com/wellcaffeinated/PhysicsJS",
		"https://github.com/amsul/pickadate.js/",
		"https://github.com/scottjehl/picturefill",
		"https://github.com/pixijs/pixi.js",
		"https://github.com/mattermost/platform",
		"https://github.com/plotly/plotly.js",
		"https://github.com/Selz/plyr",
		"https://github.com/Unitech/PM2/",
		"https://github.com/Polymer/polymer",
		"https://github.com/NetEase/pomelo",
		"https://github.com/then/promise",
		"https://github.com/flatiron/prompt/",
		"https://github.com/angular/protractor",
		"https://github.com/purifycss/purifycss",
		"https://github.com/kriskowal/q",
		"https://github.com/hapijs/qs",
		"https://github.com/quilljs/quill",
		"https://github.com/jquery/qunit",
		"https://github.com/jrburke/r.js/",
		"https://github.com/maroslaw/rainyday.js",
		"https://github.com/DmitryBaranovskiy/raphael/",
		"https://github.com/twbs/ratchet",
		"https://github.com/densitydesign/raw",
		"https://github.com/facebook/react",
		"https://github.com/react-bootstrap/react-bootstrap",
		"https://github.com/rackt/react-router",
		"https://github.com/kriasoft/react-starter-kit",
		"https://github.com/isaacs/readable-stream",
		"https://github.com/okfn/recline",
		"https://github.com/rackt/redux",
		"https://github.com/facebook/relay",
		"https://github.com/rendrjs/rendr",
		"https://github.com/request/request",
		"https://github.com/jrburke/requirejs",
		"https://github.com/scottjehl/Respond",
		"https://github.com/resume/resume.github.com",
		"https://github.com/hakimel/reveal.js",
		"https://github.com/shutterstock/rickshaw",
		"https://github.com/isaacs/rimraf/",
		"https://github.com/riot/riot",
		"https://github.com/Reactive-Extensions/RxJS",
		"https://github.com/balderdashy/sails/",
		"https://github.com/sahat/satellizer",
		"https://github.com/janpaepke/ScrollMagic",
		"https://github.com/jlmakes/scrollreveal.js",
		"https://github.com/select2/select2",
		"https://github.com/semantic-org/semantic-ui/",
		"https://github.com/arturadib/shelljs",
		"https://github.com/badges/shields",
		"https://github.com/shouldjs/should.js",
		"https://github.com/jacomyal/sigma.js",
		"https://github.com/jquery/sizzle",
		"https://github.com/Prinzhorn/skrollr",
		"https://github.com/tripit/slate",
		"https://github.com/slate/slate",
		"https://github.com/kenwheeler/slick/",
		"https://github.com/mleibman/SlickGrid",
		"https://github.com/Mango/slideout",
		"https://github.com/jwagner/smartcrop.js",
		"https://github.com/daniel-lundin/snabbt.js",
		"https://github.com/jakiestfu/Snap.js",
		"https://github.com/Automattic/socket.io/",
		"https://github.com/Automattic/socket.io-client",
		"https://github.com/RubaXa/Sortable",
		"https://github.com/fgnass/spin.js",
		"https://github.com/spine/spine",
		"https://github.com/bendc/sprint",
		"https://github.com/benweet/stackedit",
		"https://github.com/feross/standard",
		"https://github.com/etsy/statsd",
		"https://github.com/Strider-CD/strider",
		"https://github.com/stylus/stylus/",
		"https://github.com/visionmedia/superagent",
		"https://github.com/swagger-api/swagger-ui",
		"https://github.com/t4t5/sweetalert",
		"https://github.com/thebird/Swipe",
		"https://github.com/nolimits4web/swiper/",
		"https://github.com/telescopejs/telescope/",
		"https://github.com/HubSpot/tether",
		"https://github.com/mrdoob/three.js",
		"https://github.com/dominictarr/through",
		"https://github.com/rvagg/through2",
		"https://github.com/NUKnightLab/TimelineJS/",
		"https://github.com/sbstjn/timesheet.js",
		"https://github.com/tommoor/tinycon/",
		"https://github.com/CodeSeven/toastr",
		"https://github.com/tastejs/todomvc",
		"https://github.com/google/traceur-compiler",
		"https://github.com/qrohlf/trianglify",
		"https://github.com/blasten/turn.js",
		"https://github.com/jonobr1/two.js/",
		"https://github.com/twitter/typeahead.js/",
		"https://github.com/chrisaljoudi/ublock",
		"https://github.com/gorhill/uBlock",
		"https://github.com/mishoo/UglifyJS2/",
		"https://github.com/angular-ui/ui-router",
		"https://github.com/jashkenas/underscore",
		"https://github.com/epeli/underscore.string",
		"https://github.com/FredrikNoren/ungit",
		"https://github.com/AlexNisnevich/untrusted",
		"https://github.com/chriso/validator.js",
		"https://github.com/vega/vega",
		"https://github.com/julianshapiro/velocity",
		"https://github.com/videojs/video.js",
		"https://github.com/Matt-Esch/virtual-dom",
		"https://github.com/maxwellito/vivus",
		"https://github.com/auchenberg/volkswagen",
		"https://github.com/Microsoft/vscode/",
		"https://github.com/vuejs/vue",
		"https://github.com/webpack/webpack",
		"https://github.com/feross/webtorrent",
		"https://github.com/flatiron/winston",
		"https://github.com/matthieua/WOW",
		"https://github.com/ryanmcgrath/wrench-js/",
		"https://github.com/einaros/ws",
		"https://github.com/xing/wysihtml5",
		"https://github.com/Raynos/xtend",
		"https://github.com/chevex/yargs",
		"https://github.com/yeoman/yosay",
		"https://github.com/madrobby/zepto",
		"https://github.com/zeroclipboard/zeroclipboard",
		"https://github.com/gabrielecirulli/2048",
		"https://github.com/ajaxorg/ace",
		"https://github.com/doublespeakgames/adarkroom",
		"https://github.com/fabien-d/alertify.js/tree/0.3",
		"https://github.com/angular-app/angular-app",
		"https://github.com/angular/angular.js",
		"https://github.com/samizdatco/arbor",
		"https://github.com/caolan/async/",
		"https://github.com/atom/atom",
		"https://github.com/aws/aws-sdk-js",
		"https://github.com/babel/babel",
		"https://github.com/BabylonJS/Babylon.js",
		"https://github.com/jashkenas/backbone",
		"https://github.com/marionettejs/backbone.marionette",
		"https://github.com/yaronn/blessed-contrib",
		"https://github.com/petkaantonov/bluebird",
		"https://github.com/expressjs/body-parser",
		"https://github.com/angular-ui/bootstrap",
		"https://github.com/eternicode/bootstrap-datepicker",
		"https://github.com/dangrossman/bootstrap-daterangepicker/",
		"https://github.com/jschr/bootstrap-modal/",
		"https://github.com/silviomoreto/bootstrap-select",
		"https://github.com/jhollingworth/bootstrap-wysihtml5",
		"https://github.com/twbs/bootstrap",
		"https://github.com/bower/bower",
		"https://github.com/adobe/brackets",
		"https://github.com/harthur/brain",
		"https://github.com/mozbrick/brick",
		"https://github.com/nodejitsu/browsenpm.org",
		"https://github.com/BrowserSync/browser-sync",
		"https://github.com/mozilla/BrowserQuest",
		"https://github.com/alexwolfe/Buttons/",
		"https://github.com/masayuki0812/c3",
		"https://github.com/jessepollak/card",
		"https://github.com/n1k0/casperjs",
		"https://github.com/AnalyticalGraphicsInc/cesium",
		"https://github.com/sindresorhus/chalk",
		"https://github.com/nnnick/Chart.js",
		"https://github.com/gionkunz/chartist-js",
		"https://github.com/MatthewMueller/cheerio",
		"https://github.com/GoalSmashers/clean-css",
		"https://github.com/medikoo/cli-color",
		"https://github.com/ellisonleao/clumsy-bird",
		"https://github.com/NeXTs/Clusterize.js",
		"https://github.com/cocos2d/cocos2d-html5",
		"https://github.com/codemirror/codemirror",
		"https://github.com/jashkenas/coffeescript",
		"https://github.com/jackmoore/colorbox",
		"https://github.com/Marak/colors.js",
		"https://github.com/tj/commander.js",
		"https://github.com/componentjs/component",
		"http://github.com/maxogden/concat-stream/",
		"https://github.com/senchalabs/connect",
		"https://github.com/karpathy/convnetjs",
		"https://github.com/craftyjs/Crafty",
		"https://github.com/square/crossfilter",
		"https://github.com/square/cubism",
		"https://github.com/piqnt/cutjs",
		"https://github.com/mbostock/d3",
		"https://github.com/keen/dashboards",
		"https://github.com/Shopify/dashing",
		"https://github.com/alanshaw/david-www",
		"https://github.com/dc-js/dc.js",
		"https://github.com/visionmedia/debug",
		"https://github.com/imakewebthings/deck.js",
		"https://github.com/joemccann/dillinger",
		"https://github.com/bevacqua/dragula",
		"https://github.com/enyo/dropzone/",
		"https://github.com/michaelvillar/dynamics.js",
		"https://github.com/CreateJS/EaselJS",
		"https://github.com/ecomfe/echarts",
		"https://github.com/visionmedia/ejs",
		"https://github.com/emberjs/ember.js",
		"https://github.com/playcanvas/engine",
		"https://github.com/HumbleSoftware/envisionjs",
		"https://github.com/OscarGodson/EpicEditor",
		"https://github.com/es-shims/es5-shim",
		"https://github.com/Constellation/escodegen/",
		"https://github.com/ether/etherpad-lite",
		"https://github.com/strongloop/express",
		"https://github.com/kangax/fabric.js",
		"https://github.com/Marak/faker.js",
		"https://github.com/Netflix/falcor",
		"https://github.com/ftlabs/fastclick",
		"https://github.com/github/fetch",
		"https://github.com/FineUploader/fine-uploader",
		"https://github.com/davatron5000/FitVids.js",
		"https://github.com/designmodo/Flat-UI",
		"https://github.com/woothemes/FlexSlider",
		"https://github.com/flightjs/flight",
		"https://github.com/flot/flot",
		"https://github.com/facebook/flux",
		"https://github.com/foreverjs/forever",
		"https://github.com/malsup/form/",
		"https://github.com/zurb/foundation-sites",
		"https://github.com/aurelia/framework",
		"https://github.com/ExactTarget/fuelux",
		"https://github.com/fullcalendar/fullcalendar",
		"https://github.com/alvarotrigo/fullPage.js",
		"https://github.com/yeoman/generator",
		"https://github.com/TryGhost/Ghost",
		"https://github.com/hpneo/gmaps",
		"https://github.com/grafana/grafana",
		"https://github.com/greensock/GreenSock-JS",
		"https://github.com/marmelab/gremlins.js",
		"https://github.com/ducksboard/gridster.js/",
		"https://github.com/gruntjs/grunt",
		"https://github.com/gulpjs/gulp",
		"https://github.com/wearefractal/gulp-util/",
		"https://github.com/hacksalot/HackMyResume",
		"https://github.com/hammerjs/hammer.js",
		"https://github.com/wycats/handlebars.js/",
		"https://github.com/handsontable/handsontable",
		"https://github.com/hexojs/hexo/",
		"https://github.com/Hextris/hextris",
		"https://github.com/highcharts/highcharts",
		"https://github.com/isagalaev/highlight.js",
		"https://github.com/redis/hiredis-node",
		"https://github.com/twitter/hogan.js/",
		"https://github.com/imsky/holder",
		"https://github.com/goldfire/howler.js/",
		"https://github.com/niklasvh/html2canvas",
		"https://github.com/h5bp/html5-boilerplate",
		"https://github.com/ashtuchkin/iconv-lite",
		"https://github.com/codio/iloveopensource",
		"https://github.com/facebook/immutable-js",
		"https://github.com/impress/impress.js",
		"https://github.com/infinite-scroll/infinite-scroll",
		"https://github.com/SBoudrias/Inquirer.js",
		"https://github.com/taye/interact.js",
		"https://github.com/usablica/intro.js/",
		"https://github.com/driftyco/ionic",
		"https://github.com/cubiq/iscroll/",
		"https://github.com/metafizzy/isotope",
		"https://github.com/gotwarlost/istanbul",
		"https://github.com/visionmedia/jade",
		"https://github.com/jasmine/jasmine",
		"https://github.com/jquery/jquery",
		"https://github.com/tuupola/jquery_lazyload",
		"https://github.com/blueimp/jQuery-File-Upload",
		"https://github.com/aterrien/jQuery-Knob",
		"https://github.com/kamens/jQuery-menu-aim",
		"https://github.com/jquery/jquery-mobile",
		"https://github.com/defunkt/jquery-pjax",
		"https://github.com/mathiasbynens/jquery-placeholder",
		"https://github.com/jquery/jquery-ui",
		"https://github.com/jzaefferer/jquery-validation",
		"https://github.com/briangonzalez/jquery.adaptive-backgrounds.js",
		"https://github.com/nodeca/js-yaml",
		"https://github.com/jsdoc3/jsdoc",
		"https://github.com/tmpvar/jsdom",
		"https://github.com/jshint/jshint",
		"https://github.com/douglascrockford/JSON-js",
		"https://github.com/typicode/json-server",
		"https://github.com/MrRio/jsPDF",
		"https://github.com/VerbalExpressions/JSVerbalExpressions",
		"https://github.com/karma-runner/karma",
		"https://github.com/kartograph/kartograph.js",
		"https://github.com/madrobby/keymaster/",
		"https://github.com/keystonejs/keystone",
		"https://github.com/docker/kitematic",
		"https://github.com/gamelab/kiwi.js",
		"https://github.com/prawnsalad/KiwiIRC",
		"https://github.com/knockout/knockout",
		"https://github.com/loadfive/Knwl.js",
		"https://github.com/koajs/koa",
		"https://github.com/aFarkas/lazysizes",
		"https://github.com/Leaflet/Leaflet",
		"https://github.com/less/less.js",
		"https://github.com/sdelements/lets-chat",
		"https://github.com/davatron5000/Lettering.js",
		"https://github.com/javve/list.js/",
		"https://github.com/emgram769/live4chan",
		"https://github.com/mozilla/localForage",
		"https://github.com/lodash/lodash",
		"https://github.com/nomiddlename/log4js-node",
		"https://github.com/strongloop/loopback",
		"https://github.com/google/lovefield",
		"https://github.com/LazerUnicorns/lycheeJS",
		"https://github.com/dimsemenov/Magnific-Popup",
		"https://github.com/adam-p/markdown-here/",
		"https://github.com/evilstreak/markdown-js",
		"https://github.com/chjj/marked",
		"https://github.com/chipersoft/mash.li",
		"https://github.com/desandro/masonry",
		"https://github.com/angular/material",
		"https://github.com/callemall/material-ui",
		"https://github.com/linnovate/mean",
		"https://github.com/jansmolders86/mediacenterjs",
		"https://github.com/yabwe/medium-editor",
		"https://github.com/melonjs/melonJS",
		"https://github.com/knsv/mermaid",
		"https://github.com/meteor/meteor",
		"https://github.com/mozilla/metrics-graphics/",
		"https://github.com/olton/Metro-UI-CSS/",
		"https://github.com/isaacs/minimatch/",
		"https://github.com/substack/minimist",
		"https://github.com/lhorie/mithril.js",
		"https://github.com/mochajs/mocha",
		"https://github.com/Modernizr/Modernizr",
		"https://github.com/stamen/modestmaps-js",
		"https://github.com/moment/moment",
		"https://github.com/Automattic/mongoose",
		"https://github.com/morrisjs/morris.js/",
		"https://github.com/ccampbell/mousetrap",
		"https://github.com/janl/mustache.js",
		"https://github.com/nylas/n1/",
		"https://github.com/flatiron/nconf/",
		"https://github.com/AvianFlu/ncp",
		"https://github.com/npm/newww",
		"https://github.com/ngbp/ngbp",
		"https://github.com/segmentio/nightmare",
		"https://github.com/nodejs/node",
		"https://github.com/noderedis/node_redis/",
		"https://github.com/substack/node-browserify",
		"https://github.com/justmoon/node-extend",
		"https://github.com/jprichardson/node-fs-extra",
		"https://github.com/isaacs/node-glob/",
		"https://github.com/isaacs/node-graceful-fs",
		"https://github.com/nodejitsu/node-http-proxy",
		"https://github.com/node-inspector/node-inspector",
		"https://github.com/jscs-dev/node-jscs",
		"https://github.com/broofa/node-mime/",
		"https://github.com/substack/node-mkdirp",
		"https://github.com/felixge/node-mysql",
		"https://github.com/ciaranj/node-oauth/",
		"https://github.com/pwnall/node-open",
		"https://github.com/substack/node-optimist",
		"https://github.com/isaacs/node-semver",
		"https://github.com/shtylman/node-uuid",
		"https://github.com/NodeBB/NodeBB",
		"https://github.com/nodemailer/nodemailer",
		"https://github.com/harthur/nomnom/",
		"https://github.com/isaacs/nopt/",
		"https://github.com/jaredreich/notie.js",
		"https://github.com/needim/noty",
		"https://github.com/npm/npm",
		"https://github.com/npm/npm-www",
		"https://github.com/rstacruz/nprogress",
		"https://github.com/novus/nvd3",
		"https://github.com/jimhigson/oboe.js",
		"https://github.com/peachananr/onepage-scroll",
		"https://github.com/ericf/open-marriage",
		"https://github.com/sindresorhus/pageres",
		"https://github.com/paperjs/paper.js",
		"https://github.com/ParsePlatform/parse-server",
		"https://github.com/guillaumepotier/Parsley.js",
		"https://github.com/VincentGarreau/particles.js",
		"https://github.com/jaredhanson/passport",
		"https://github.com/qiao/PathFinding.js",
		"https://github.com/mozilla/pdf.js",
		"https://github.com/benpickles/peity",
		"https://github.com/pencilblue/pencilblue",
		"https://github.com/photonstorm/phaser",
		"https://github.com/dimsemenov/PhotoSwipe",
		"https://github.com/wellcaffeinated/PhysicsJS",
		"https://github.com/amsul/pickadate.js/",
		"https://github.com/scottjehl/picturefill",
		"https://github.com/pixijs/pixi.js",
		"https://github.com/mattermost/platform",
		"https://github.com/plotly/plotly.js",
		"https://github.com/Selz/plyr",
		"https://github.com/Unitech/PM2/",
		"https://github.com/Polymer/polymer",
		"https://github.com/NetEase/pomelo",
		"https://github.com/then/promise",
		"https://github.com/flatiron/prompt/",
		"https://github.com/angular/protractor",
		"https://github.com/purifycss/purifycss",
		"https://github.com/kriskowal/q",
		"https://github.com/hapijs/qs",
		"https://github.com/quilljs/quill",
		"https://github.com/jquery/qunit",
		"https://github.com/jrburke/r.js/",
		"https://github.com/maroslaw/rainyday.js",
		"https://github.com/DmitryBaranovskiy/raphael/",
		"https://github.com/twbs/ratchet",
		"https://github.com/densitydesign/raw",
		"https://github.com/facebook/react",
		"https://github.com/react-bootstrap/react-bootstrap",
		"https://github.com/rackt/react-router",
		"https://github.com/kriasoft/react-starter-kit",
		"https://github.com/isaacs/readable-stream",
		"https://github.com/okfn/recline",
		"https://github.com/rackt/redux",
		"https://github.com/facebook/relay",
		"https://github.com/rendrjs/rendr",
		"https://github.com/request/request",
		"https://github.com/jrburke/requirejs",
		"https://github.com/scottjehl/Respond",
		"https://github.com/resume/resume.github.com",
		"https://github.com/hakimel/reveal.js",
		"https://github.com/shutterstock/rickshaw",
		"https://github.com/isaacs/rimraf/",
		"https://github.com/riot/riot",
		"https://github.com/Reactive-Extensions/RxJS",
		"https://github.com/balderdashy/sails/",
		"https://github.com/sahat/satellizer",
		"https://github.com/janpaepke/ScrollMagic",
		"https://github.com/jlmakes/scrollreveal.js",
		"https://github.com/select2/select2",
		"https://github.com/semantic-org/semantic-ui/",
		"https://github.com/arturadib/shelljs",
		"https://github.com/badges/shields",
		"https://github.com/shouldjs/should.js",
		"https://github.com/jacomyal/sigma.js",
		"https://github.com/jquery/sizzle",
		"https://github.com/Prinzhorn/skrollr",
		"https://github.com/tripit/slate",
		"https://github.com/slate/slate",
		"https://github.com/kenwheeler/slick/",
		"https://github.com/mleibman/SlickGrid",
		"https://github.com/Mango/slideout",
		"https://github.com/jwagner/smartcrop.js",
		"https://github.com/daniel-lundin/snabbt.js",
		"https://github.com/jakiestfu/Snap.js",
		"https://github.com/Automattic/socket.io/",
		"https://github.com/Automattic/socket.io-client",
		"https://github.com/RubaXa/Sortable",
		"https://github.com/fgnass/spin.js",
		"https://github.com/spine/spine",
		"https://github.com/bendc/sprint",
		"https://github.com/benweet/stackedit",
		"https://github.com/feross/standard",
		"https://github.com/etsy/statsd",
		"https://github.com/Strider-CD/strider",
		"https://github.com/stylus/stylus/",
		"https://github.com/visionmedia/superagent",
		"https://github.com/swagger-api/swagger-ui",
		"https://github.com/t4t5/sweetalert",
		"https://github.com/thebird/Swipe",
		"https://github.com/nolimits4web/swiper/",
		"https://github.com/telescopejs/telescope/",
		"https://github.com/HubSpot/tether",
		"https://github.com/mrdoob/three.js",
		"https://github.com/dominictarr/through",
		"https://github.com/rvagg/through2",
		"https://github.com/NUKnightLab/TimelineJS/",
		"https://github.com/sbstjn/timesheet.js",
		"https://github.com/tommoor/tinycon/",
		"https://github.com/CodeSeven/toastr",
		"https://github.com/tastejs/todomvc",
		"https://github.com/google/traceur-compiler",
		"https://github.com/qrohlf/trianglify",
		"https://github.com/blasten/turn.js",
		"https://github.com/jonobr1/two.js/",
		"https://github.com/twitter/typeahead.js/",
		"https://github.com/chrisaljoudi/ublock",
		"https://github.com/gorhill/uBlock",
		"https://github.com/mishoo/UglifyJS2/",
		"https://github.com/angular-ui/ui-router",
		"https://github.com/jashkenas/underscore",
		"https://github.com/epeli/underscore.string",
		"https://github.com/FredrikNoren/ungit",
		"https://github.com/AlexNisnevich/untrusted",
		"https://github.com/chriso/validator.js",
		"https://github.com/vega/vega",
		"https://github.com/julianshapiro/velocity",
		"https://github.com/videojs/video.js",
		"https://github.com/Matt-Esch/virtual-dom",
		"https://github.com/maxwellito/vivus",
		"https://github.com/auchenberg/volkswagen",
		"https://github.com/Microsoft/vscode/",
		"https://github.com/vuejs/vue",
		"https://github.com/webpack/webpack",
		"https://github.com/feross/webtorrent",
		"https://github.com/flatiron/winston",
		"https://github.com/matthieua/WOW",
		"https://github.com/ryanmcgrath/wrench-js/",
		"https://github.com/einaros/ws",
		"https://github.com/xing/wysihtml5",
		"https://github.com/Raynos/xtend",
		"https://github.com/chevex/yargs",
		"https://github.com/yeoman/yosay",
		"https://github.com/madrobby/zepto",
		"https://github.com/zeroclipboard/zeroclipboard"*/
	};


	//private static String repoURL = "https://github.com/twbs/bootstrap";

	private static WebDriver driver;

	public static void main(String[] args) throws Exception {
		driver = new FirefoxDriver();

		for (int i = 0; i< repoList.length; i++){

			//clone the repository because TestCodePropertyAnalyzer.java and CoverageCalculator.java need it
			RepoCloner.cloneRepo(repoList[i]);

			// Load the html test runner in the browser to get coverage
			driver.get(repoList[i]);
			System.out.print("Loading the URL " + repoList[i]);

			// make sure all tests are finished before getting the trace
			waitForPageToLoad();

			List<WebElement> socials = driver.findElements(By.xpath("//a[@class='Link--muted']"));
			System.out.println(socials.get(4).getText() + "\t");   //stars
			System.out.println(socials.get(5).getText() + "\t");   //watching
			System.out.println(socials.get(6).getText() + "\t");   //forks

			List<WebElement> nums = driver.findElements(By.xpath("//a[@class='Link--primary no-underline']"));
			System.out.println(nums.get(0).getText() + "\t");    //realeses
			System.out.println(nums.get(1).getText() + "\t");     //used by

			List<WebElement> other = driver.findElements(By.xpath("//a[@class='ml-3 Link--primary no-underline']"));
			System.out.println(other.get(0).getText() + "\t");   //branches
			System.out.println(other.get(1).getText() + "\t");   //tags

			List<WebElement> commits = driver.findElements(By.xpath("//span[@class='d-none d-sm-inline']"));
			System.out.println(commits.get(1).getText() + "\t");   //commits

			List<WebElement> contributors = driver.findElements(By.xpath("//span[@class='Counter ml-1']"));
			System.out.println(contributors.get(1).getText() + " contributors\t");   //contributors

		}

		driver.quit();
	}



	public void driverExecute(String javascript) throws Exception {
		((JavascriptExecutor) driver).executeScript(javascript);
	}


	private static void waitForPageToLoad() {  // could be used to make sure the js code execution happens after the page is fully loaded
		String pageLoadStatus = null;
		do {
			pageLoadStatus = (String)((JavascriptExecutor) driver).executeScript("return document.readyState");
			//System.out.print(".");
		} while (!pageLoadStatus.equals("complete"));
		System.out.println();
		System.out.println("Page Loaded.");
	}

}
