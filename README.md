# Introduction

Elastic-Grok-Script-Plugin is a provider of Grok ElasticSearch plug-in

# Install

* fork this project and change the `elasticsearch` in maven ,default is 2.3.3
* run `mvn assembly:assembly` ,you can find the release in `target/release`
* copy the `pattern` folder and `plugin-descriptor.properties` file to your plugin folder
* you can put your own pattern to pattern folder
* restart your elasticsearch
* Have Fun (○’ω’○)

# Params

Name|Desc
----|----
fieldName|which field in source do you want to grok
pattern| your grok pattern
groupkeys|you can use this param to get the field after grok or the file in source
,if you want to get multi fields ,you can use like this `a,b,c`

# Sample

* Use Grok to Extract Fields Extract Fields

        {
          "query": {
            "match_all": {}
          },
          "_source": {
            "includes": [],
            "excludes": []
          },
          "script_fields": {
            "test2": {
              "params": {
                "fieldName": "message",
                "pattern": "%{WORD:wd}",
                "groupkeys": "wd"
              },
              "script": "grokscript",
              "lang": "native"
            }
          },
          "size": 1
        }

* Do Some Aggregation

        {
          "query": {
            "match_all": {}
          },
          "_source": {
            "includes": [],
            "excludes": []
          },
          "aggs": {
            "counts_by_my_field": {
              "stats": {
                "params": {
                  "fieldName": "message",
                  "pattern": "%{WORD:wd}",
                  "groupkeys": "wd"
                },
                "script": "grokscript",
                "lang": "native"
              }
            }
          },
          "size": 1
        }

* GroupBy Extract Field and Do More Aggregation

        {
            "query": {
                "match_all": {}
            },
            "_source": {
                "includes": [],
                "excludes": []
            },
            "aggs": {
                "counts_by_my_field": {
                    "terms": {
                        "params": {
                            "fieldName": "message",
                            "pattern": "%{WORD:wd}",
                            "groupkeys": "wd"
                        },
                        "script": "grokscript",
                        "lang": "native"
                    },
                    "aggs": {
                        "agg1": {
                            "terms": {
                                "field": "http_method"
                            }
                        }
                    }
                }
            },
            "size": 1
        }

# Howto

* if you want to load the Grok Pattern from other place,you can implement the `IGrokPatternLoader`

