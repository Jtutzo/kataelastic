# Kataelastic

## Urls util

 - https://medium.com/@sourav.pati09/how-to-use-java-high-level-rest-client-with-spring-boot-to-talk-to-aws-elasticsearch-2b6106f2e2c
 - https://www.baeldung.com/elasticsearch-java
 - https://blog.zenika.com/2013/04/29/integrer-elasticsearch-dans-une-application-java/
 - https://www.elastic.co/fr/products/elasticsearch
 - https://openclassrooms.com/fr/courses/4462426-maitrisez-les-bases-de-donnees-nosql/4474696-interrogez-des-donnees-textuelles
 
 ## Data

```json
[
  {
    "code": "code1",
    "rctCode": "2435",
    "dealName": "nom 1",
    "users": [
      "jeremy.tutzo",
      "ludovic.panel"
    ],
    "teams": [
      "353b2270-eb90-49e9-a224-79833ba31045",
      "6eb787b8-b086-463b-9878-15fafc9e7cda"
    ]
  },
  {
    "code": "code2",
    "rctCode": "2438",
    "dealName": "nom 2",
    "users": [
      "jeremy.tutzo",
      "nicolas.tremeau"
    ],
    "teams": [
      "cbbf374d-5f8c-4ed8-808e-8a6fdcaa1abc"
    ]
  }
]
```
 
## Search Request
```json
{
  "query": {
    "bool": {
      "must": [
        {
          "bool": {
            "should": [
              {
                "term": {
                  "rctCode": "code1"
                }
              },
              {
                "term": {
                  "code": "code1"
                }
              },
              {
                "fuzzy": {
                  "dealName": "code1"
                }
              }
            ]
          }
        },
        {
          "bool": {
            "should": [
              {
                "terms": {
                  "users": ["jeremy.tutzo", "gaetane.persona"]
                }
              },
              {
                "terms": {
                  "teams": ["jeremy.tutzo"]
                }
              }
            ]
          }
        }
      ]
    }
  }
}
```
