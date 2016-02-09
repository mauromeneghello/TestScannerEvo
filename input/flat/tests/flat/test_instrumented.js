test_funcionCallTrace = [];
function test_funcionCallWrapper(functionName, testFunction, functionBody) {
  test_funcionCallTrace.push({functionName: functionName, testFunction: testFunction});
  return functionBody;
}
function test_getFuncionCallTrace() {
  return test_funcionCallTrace;
}
QUnit.module("Flat.parse");
test("Flat parses single object", function() {
  var value = test_funcionCallWrapper("parse", "test_Test1", Flat.parse("name:object"));
  deepEqual(value, {name: "object"});
});
test("Flat parses single object with multiple values", function() {
  var value = test_funcionCallWrapper("parse", "test_Test2", Flat.parse("name:object\ncolor:red"));
  deepEqual(value, {name: "object", color: "red"});
});
test("Flat parses object with array", function() {
  var value = test_funcionCallWrapper("parse", "test_Test3", Flat.parse("name:object\ncolor:red\nshapes:triangle,square"));
  deepEqual(value, {name: "object", color: "red", shapes: ["triangle", "square"]});
});
test("Flat parses object and trims value end", function() {
  var value = test_funcionCallWrapper("parse", "test_Test4", Flat.parse("name:object\ncolor:red "));
  deepEqual(value, {name: "object", color: "red"});
});
test("Flat parses object and trims value beginning", function() {
  var value = test_funcionCallWrapper("parse", "test_Test5", Flat.parse("name:object\ncolor: red"));
  deepEqual(value, {name: "object", color: "red"});
});
test("Flat parses object and trims value both sides", function() {
  var value = test_funcionCallWrapper("parse", "test_Test6", Flat.parse("name:object\ncolor: red "));
  deepEqual(value, {name: "object", color: "red"});
});
test("Flat parses object and trims value in array", function() {
  var value = test_funcionCallWrapper("parse", "test_Test7", Flat.parse("name:object\ncolor:red\nshapes: triangle,square , circle "));
  deepEqual(value, {name: "object", color: "red", shapes: ["triangle", "square", "circle"]});
});
test("Flat parses object and trims key beginning", function() {
  var value = test_funcionCallWrapper("parse", "test_Test8", Flat.parse(" name:object\ncolor:red"));
  deepEqual(value, {name: "object", color: "red"});
});
test("Flat parses object and trims key end", function() {
  var value = test_funcionCallWrapper("parse", "test_Test9", Flat.parse("name :object\ncolor:red"));
  deepEqual(value, {name: "object", color: "red"});
});
test("Flat parses object and trims key both sides", function() {
  var value = test_funcionCallWrapper("parse", "test_Test10", Flat.parse(" name :object\ncolor:red"));
  deepEqual(value, {name: "object", color: "red"});
});
test("Flat parses object and empty value is undefined", function() {
  var value = test_funcionCallWrapper("parse", "test_Test11", Flat.parse("name:object\ncolor:red\nnewAttr:"));
  deepEqual(value, {name: "object", color: "red", newAttr: undefined});
});
test("Flat parses multiple objects", function() {
  var value = test_funcionCallWrapper("parse", "test_Test12", Flat.parse("name:object\ncolor:red\n\nname:object2\ncolor:blue"));
  deepEqual(value, [{name: "object", color: "red"}, {name: "object2", color: "blue"}]);
});
test("Flat parses multiple objects with extra lines", function() {
  var value = test_funcionCallWrapper("parse", "test_Test13", Flat.parse("name:object\ncolor:red\n\n\nname:object2\ncolor:blue"));
  deepEqual(value, [{name: "object", color: "red"}, {name: "object2", color: "blue"}]);
});
test("Flat parses multiple objects and omits empties", function() {
  var value = test_funcionCallWrapper("parse", "test_Test14", Flat.parse("name:object\ncolor:red\n\n\n\nname:object2\ncolor:blue"));
  deepEqual(value, [{name: "object", color: "red"}, {name: "object2", color: "blue"}]);
});
test("Flat parses object and removes preceeding newlines", function() {
  var value = test_funcionCallWrapper("parse", "test_Test15", Flat.parse("\n\nname:object\ncolor:red"));
  deepEqual(value, {name: "object", color: "red"});
});
test("Flat parses object and removes trailing newlines", function() {
  var value = test_funcionCallWrapper("parse", "test_Test16", Flat.parse("name:object\ncolor:red\n"));
  deepEqual(value, {name: "object", color: "red"});
});
test("Flat parses object with int type", function() {
  var value = test_funcionCallWrapper("parse", "test_Test17", Flat.parse("name:object\ncounter:1"));
  deepEqual(value, {name: "object", counter: 1});
});
test("Flat parses object with date type", function() {
  var value = test_funcionCallWrapper("parse", "test_Test18", Flat.parse("name:object\ndate:1/1/1956"));
  deepEqual(value, {name: "object", date: new Date("1/1/1956")});
});
test("Flat parses array with int type", function() {
  var value = test_funcionCallWrapper("parse", "test_Test19", Flat.parse("name:object\ncounter:1,2,3"));
  deepEqual(value, {name: "object", counter: [1, 2, 3]});
});
test("Flat parses array with date type", function() {
  var value = test_funcionCallWrapper("parse", "test_Test20", Flat.parse("name:object\ndate:1/1/1956,2/2/1990"));
  deepEqual(value, {name: "object", date: [new Date("1/1/1956"), new Date("2/2/1990")]});
});
test("Flat field will ignore empty preceeding array values", function() {
  var value = test_funcionCallWrapper("parse", "test_Test21", Flat.parse("name:object\narray:,two,three"));
  deepEqual(value, {name: "object", array: ["two", "three"]});
});
test("Flat field will ignore empty trailing array values", function() {
  var value = test_funcionCallWrapper("parse", "test_Test22", Flat.parse("name:object\narray:two,three,,"));
  deepEqual(value, {name: "object", array: ["two", "three"]});
});
test("Flat field will ignore empty inbetween array values", function() {
  var value = test_funcionCallWrapper("parse", "test_Test23", Flat.parse("name:object\narray:two,,,three"));
  deepEqual(value, {name: "object", array: ["two", "three"]});
});
test("Flat field can be hinted into array of one (post)", function() {
  var value = test_funcionCallWrapper("parse", "test_Test24", Flat.parse("name:object\narray:one,"));
  deepEqual(value, {name: "object", array: ["one"]});
});
test("Flat field can be hinted into array of one (pre)", function() {
  var value = test_funcionCallWrapper("parse", "test_Test25", Flat.parse("name:object\narray:one,"));
  deepEqual(value, {name: "object", array: ["one"]});
});
test("Flat field can be hinted into array of zero", function() {
  var value = test_funcionCallWrapper("parse", "test_Test26", Flat.parse("name:object\narray:,"));
  deepEqual(value, {name: "object", array: []});
});
test("Flat field will produce empty array for all empty values", function() {
  var value = test_funcionCallWrapper("parse", "test_Test27", Flat.parse("name:object\narray:,,,,"));
  deepEqual(value, {name: "object", array: []});
});
test("Flat field will ignore whitespace preceeding array values", function() {
  var value = test_funcionCallWrapper("parse", "test_Test28", Flat.parse("name:object\narray: ,two,three"));
  deepEqual(value, {name: "object", array: ["two", "three"]});
});
test("Flat field will ignore whitespace trailing array values", function() {
  var value = test_funcionCallWrapper("parse", "test_Test29", Flat.parse("name:object\narray:two,three, "));
  deepEqual(value, {name: "object", array: ["two", "three"]});
});
test("Flat field will ignore whitespace inbetween array values", function() {
  var value = test_funcionCallWrapper("parse", "test_Test30", Flat.parse("name:object\narray:two, ,three"));
  deepEqual(value, {name: "object", array: ["two", "three"]});
});
QUnit.module("Flat.toJson");
test("Flat will transform to JSON", function() {
  var value = test_funcionCallWrapper("toJson", "test_Test31", Flat.toJson("name:object\narray:two, ,three"));
  equal(value, "{\"name\":\"object\",\"array\":[\"two\",\"three\"]}");
});
QUnit.module("Flat.toCsv");
test("Flat will transform to csv, one", function() {
  var value = test_funcionCallWrapper("toCsv", "test_Test32", Flat.toCsv("name:object\ncolor:red"));
  equal(value, "object,red");
});
test("Flat will transform to csv, symmetric columns", function() {
  var value = test_funcionCallWrapper("toCsv", "test_Test33", Flat.toCsv("name:object\ncolor:red\n\nname:object2\ncolor:blue"));
  equal(value, "object,red\nobject2,blue");
});
test("Flat will transform to csv, asymmetric columns", function() {
  var value = test_funcionCallWrapper("toCsv", "test_Test34", Flat.toCsv("name:object\ncolor:red\n\nname:object2\nshape:circle"));
  equal(value, "object,red,\nobject2,,circle");
});
QUnit.module("Flat.fromObject");
test("Flat will convert object", function() {
  var value = test_funcionCallWrapper("fromObject", "test_Test35", Flat.fromObject({name: "object", color: "red"}));
  equal(value, "name:object\ncolor:red");
});
test("Flat will truncate deep object", function() {
  var value = test_funcionCallWrapper("fromObject", "test_Test36", Flat.fromObject({name: "object", color: "red", attrs: {key: "value1", key2: "value2"}}));
  equal(value, "name:object\ncolor:red\nattrs:[object Object]");
});
test("Flat will convert object with array", function() {
  var value = test_funcionCallWrapper("fromObject", "test_Test37", Flat.fromObject({name: "object", color: "red", shape: ["triangle", "square"]}));
  equal(value, "name:object\ncolor:red\nshape:triangle,square");
});
test("Flat will convert array", function() {
  var value = test_funcionCallWrapper("fromObject", "test_Test38", Flat.fromObject([{name: "object", color: "red"}, {name: "object2", color: "blue"}]));
  equal(value, "name:object\ncolor:red\n\nname:object2\ncolor:blue");
});
