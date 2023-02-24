package com.a2z.app.ui.screen.indonepal

import com.a2z.app.data.model.indonepal.INStaticData
import com.a2z.app.data.model.indonepal.INStaticServiceData
import com.google.gson.Gson

object INUtil {

    private const val STATIC_RAW_DATA = """
        {
  "gender": [
    {
      "id": "Male",
      "name": "Male"
    },
    {
      "id": "Female",
      "name": "Female"
    },
    {
      "id": "Other",
      "name": "Other"
    }
  ],
  "IdType": [
    {
      "id": "Aadhaar Card",
      "name": "Aadhaar Card"
    },
    {
      "id": "Indian Driving License",
      "name": "Indian Driving License"
    },
    {
      "id": "Nepalese Citizenship",
      "name": "Nepalese Citizenship"
    },
    {
      "id": "Nepalese Passport",
      "name": "Nepalese Passport"
    },
    {
      "id": "Pan Card",
      "name": "Pan Card"
    }
  ],
  "nationality": {
    "id": "Nepalese",
    "name": "Nepalese"
  },
  "incomeSource": [
    {
      "id": "Salary",
      "name": "Salary"
    },
    {
      "id": "Business",
      "name": "Business"
    },
    {
      "id": "Saving",
      "name": "Saving"
    },
    {
      "id": "Gift",
      "name": "Gift"
    },
    {
      "id": "Lotery",
      "name": "Lotery"
    },
    {
      "id": "Other",
      "name": "Other"
    }
  ],
  "relationShip": [
    {
      "id": "Son",
      "name": "Son"
    },
    {
      "id": "Wife",
      "name": "Wife"
    },
    {
      "id": "Aunti",
      "name": "Aunti"
    },
    {
      "id": "Brother",
      "name": "Brother"
    },
    {
      "id": "Sister",
      "name": "Sister"
    },
    {
      "id": "Daughter",
      "name": "Daughter"
    },
    {
      "id": "Father",
      "name": "Father"
    },
    {
      "id": "Husband",
      "name": "Husband"
    },
    {
      "id": "Mother",
      "name": "Mother"
    },
    {
      "id": "Nephew",
      "name": "Nephew"
    },
    {
      "id": "Niece",
      "name": "Niece"
    },
    {
      "id": "Cousin",
      "name": "Cousin"
    },
    {
      "id": "Uncle",
      "name": "Uncle"
    },
    {
      "id": "Friend",
      "name": "Friend"
    },
    {
      "id": "Grand Father",
      "name": "Grand Father"
    },
    {
      "id": "Grand Mother",
      "name": "Grand Mother"
    },
    {
      "id": "Grand Mother",
      "name": "Grand Mother"
    },
    {
      "id": "Mother in Law",
      "name": "Mother in Law"
    },
    {
      "id": "Sister in Law",
      "name": "Sister in Law"
    },
    {
      "id": "Father in Law",
      "name": "Father in Law"
    },
    {
      "id": "Brother in Law",
      "name": "Brother in Law"
    },
    {
      "id": "Boy Friend",
      "name": "Boy Friend"
    },
    {
      "id": "Girl Friend",
      "name": "Girl Friend"
    },
    {
      "id": "Other",
      "name": "Other"
    }
  ],
  "paymentMode": [
    {
      "id": "Account Deposit",
      "name": "Account Deposit"
    },
    {
      "id": "Cash Payment",
      "name": "Cash Payment"
    }
  ],
  "purposeOfRemitence": [
    {
      "id": "Family Maintenance",
      "name": "Family Maintenance"
    },
    {
      "id": "Medical",
      "name": "Medical"
    },
    {
      "id": "Education",
      "name": "Education"
    },
    {
      "id": "Others",
      "name": "Others"
    }
  ],
  "stateLists": [
    {
      "id": 1,
      "name": "Andaman and Nicobar (UT)"
    },
    {
      "id": 2,
      "name": "Andhra Pradesh"
    },
    {
      "id": 3,
      "name": "Arunachal Pradesh"
    },
    {
      "id": 4,
      "name": "Assam"
    },
    {
      "id": 5,
      "name": "Chandigarh (UT)"
    },
    {
      "id": 6,
      "name": "Chhattisgarh"
    },
    {
      "id": 7,
      "name": "Dadra and Nagar Haveli (UT)"
    },
    {
      "id": 8,
      "name": "Daman and Diu (UT)"
    },
    {
      "id": 9,
      "name": "Goa"
    },
    {
      "id": 10,
      "name": "Gujarat"
    },
    {
      "id": 11,
      "name": "Haryana"
    },
    {
      "id": 12,
      "name": "Himachal Pradesh"
    },
    {
      "id": 13,
      "name": "Jammu and Kashmir"
    },
    {
      "id": 14,
      "name": "Jharkhand"
    },
    {
      "id": 15,
      "name": "Karnataka"
    },
    {
      "id": 16,
      "name": "Kerala"
    },
    {
      "id": 17,
      "name": "Lakshadweep (UT)"
    },
    {
      "id": 18,
      "name": "Madhya Pradesh"
    },
    {
      "id": 19,
      "name": "Manipur"
    },
    {
      "id": 20,
      "name": "Meghalaya"
    },
    {
      "id": 21,
      "name": "Mizoram"
    },
    {
      "id": 22,
      "name": "Nagaland"
    },
    {
      "id": 23,
      "name": "National Capital Territory of Delhi (UT)"
    },
    {
      "id": 24,
      "name": "Odisha"
    },
    {
      "id": 25,
      "name": "Puducherry (UT)"
    },
    {
      "id": 26,
      "name": "Punjab"
    },
    {
      "id": 27,
      "name": "Rajasthan"
    },
    {
      "id": 28,
      "name": "Sikkim"
    },
    {
      "id": 29,
      "name": "Tamil Nadu"
    },
    {
      "id": 30,
      "name": "Telangana"
    },
    {
      "id": 31,
      "name": "Tripura"
    },
    {
      "id": 32,
      "name": "Uttar Pradesh"
    },
    {
      "id": 33,
      "name": "Uttarakhand"
    },
    {
      "id": 34,
      "name": "West Bengal"
    },
    {
      "id": 35,
      "name": "Bihar"
    },
    {
      "id": 36,
      "name": "Maharashtra"
    }
  ],
  "bankName": [
    {
      "id": 1,
      "name": "AGRICULTURAL DEVELOPMENT BANK LTD"
    },
    {
      "id": 2,
      "name": "ASHA LAGHUBITTA BITTIYA SANSTHA LTD"
    },
    {
      "id": 3,
      "name": "BANK OF KATHMANDU LTD"
    },
    {
      "id": 4,
      "name": "BEST FINANCE COMPANY LTD"
    },
    {
      "id": 5,
      "name": "CENTRAL FINANCE LTD"
    },
    {
      "id": 6,
      "name": "CENTURY COMMERCIAL BANK LTD"
    },
    {
      "id": 7,
      "name": "CITIZENS BANK INTERNATIONAL LTD"
    },
    {
      "id": 8,
      "name": "CIVIL BANK LTD"
    },
    {
      "id": 9,
      "name": "EVEREST BANK LTD"
    },
    {
      "id": 10,
      "name": "EXCEL DEVELOPMENT BANK LTD"
    },
    {
      "id": 11,
      "name": "FORWARD COMMUNITY MICROFINANCE BITTIYA SANSTHA LTD"
    },
    {
      "id": 12,
      "name": "GARIMA BIKAS BANK LTD"
    },
    {
      "id": 13,
      "name": "GLOBAL IME BANK LTD"
    },
    {
      "id": 14,
      "name": "GOODWILL FINANCE LTD"
    },
    {
      "id": 15,
      "name": "GREEN DEVELOPMENT BANK LTD"
    },
    {
      "id": 16,
      "name": "GUHESHWARI MERCHANT BANKING AND FINANCE LTD"
    },
    {
      "id": 17,
      "name": "HIMALAYAN BANK LTD"
    },
    {
      "id": 18,
      "name": "ICFC FINANCE LTD"
    },
    {
      "id": 19,
      "name": "JANAKI FINANCE COMPANY LTD"
    },
    {
      "id": 20,
      "name": "JYOTI BIKASH BANK LTD"
    },
    {
      "id": 21,
      "name": "KAMANA SEWA BIKAS BANK LIMITED"
    },
    {
      "id": 22,
      "name": "KARNALI DEVELOPMENT BANK LTD"
    },
    {
      "id": 23,
      "name": "KUMARI BANK LTD"
    },
    {
      "id": 24,
      "name": "LAXMI BANK LTD"
    },
    {
      "id": 25,
      "name": "LUMBINI BIKAS BANK LIMITED"
    },
    {
      "id": 26,
      "name": "MACHHAPUCHCHHRE BANK LTD"
    },
    {
      "id": 27,
      "name": "MAHALAXMI BIKAS BANK LIMITED"
    },
    {
      "id": 28,
      "name": "MAHULI SAMUDAYIK LAGUBITTA BITTIYA SANSTHA LTD"
    },
    {
      "id": 29,
      "name": "MANJUSHREE FINANCE LTD"
    },
    {
      "id": 30,
      "name": "MEGA BANK NEPAL LTD"
    },
    {
      "id": 31,
      "name": "MERO MICRO FINANCE BITTIYA SANSTHA LTD"
    },
    {
      "id": 32,
      "name": "MITERI DEVELOPMENT BANK LTD"
    },
    {
      "id": 33,
      "name": "MUKTINATH BIKAS BANK LTD"
    },
    {
      "id": 34,
      "name": "MULTI PURPOSE FINANCE COMPANY LTD"
    },
    {
      "id": 35,
      "name": "NABIL BANK LTD"
    },
    {
      "id": 36,
      "name": "NARAYANI DEVELOPMENT BANK LTD"
    },
    {
      "id": 37,
      "name": "NCC BANK LTD"
    },
    {
      "id": 38,
      "name": "NEPAL BANGLADESH BANK LTD"
    },
    {
      "id": 39,
      "name": "NEPAL BANK LTD"
    },
    {
      "id": 40,
      "name": "NEPAL FINANCE LTD"
    },
    {
      "id": 41,
      "name": "NEPAL INVESTMENT BANK LTD"
    },
    {
      "id": 42,
      "name": "NEPAL SBI BANK LTD"
    },
    {
      "id": 43,
      "name": "NERUDE LAGHUBITTA BIKAS BANK LTD"
    },
    {
      "id": 44,
      "name": "NIC ASIA BANK LTD"
    },
    {
      "id": 45,
      "name": "NMB BANK LTD"
    },
    {
      "id": 46,
      "name": "POKHARA FINANCE LTD"
    },
    {
      "id": 47,
      "name": "PRABHU BANK LIMITED"
    },
    {
      "id": 48,
      "name": "PRABHU COOPERATIVE SERVICE LIMITED"
    },
    {
      "id": 49,
      "name": "PRIME COMMERCIAL BANK LTD"
    },
    {
      "id": 50,
      "name": "PROGRESSIVE FINANCE COMPANY LTD"
    },
    {
      "id": 51,
      "name": "RASTRIYA BANIJYA BANK LTD"
    },
    {
      "id": 52,
      "name": "RELIANCE FINANCE LTD"
    },
    {
      "id": 53,
      "name": "SAHARA BIKAS BANK LTD"
    },
    {
      "id": 54,
      "name": "SALAPA BIKAS BANK LTD"
    },
    {
      "id": 55,
      "name": "SANIMA BANK LTD"
    },
    {
      "id": 56,
      "name": "SAPTAKOSHI DEVELOPMENT BANK LTD"
    },
    {
      "id": 57,
      "name": "SHANGRILA DEVELOPMENT BANK LTD"
    },
    {
      "id": 58,
      "name": "SHINE RESUNGA DEVELOPMENT BANK LTD"
    },
    {
      "id": 59,
      "name": "SIDDHARTHA BANK LTD"
    },
    {
      "id": 60,
      "name": "SINDHU BIKASH BANK LTD"
    },
    {
      "id": 61,
      "name": "STANDARD CHARTERED BANK LTD"
    },
    {
      "id": 62,
      "name": "SUNRISE BANK LTD"
    },
    {
      "id": 63,
      "name": "SURYODAYA LAGUBITTA BITTIYA SASTHA LTD"
    },
    {
      "id": 64,
      "name": "TINAU MISSION DEVELOPMENT BANK LTD"
    },
    {
      "id": 65,
      "name": "UNITED FINANCE LTD"
    },
    {
      "id": 66,
      "name": "VIJAYA LAGUBITTA BITTIYA SASTHA LTD"
    }
  ],
  "customerType": [
    {
      "id": 1,
      "name": "Salaried"
    },
    {
      "id": 2,
      "name": "Self Employed including Professional"
    },
    {
      "id": 3,
      "name": "Farmer"
    },
    {
      "id": 4,
      "name": "Housewife"
    }
  ],
  "sourceIncomeType": [
    {
      "id": 1,
      "name": "Govt"
    },
    {
      "id": 2,
      "name": "Public sector"
    },
    {
      "id": 3,
      "name": "Private Sector"
    },
    {
      "id": 4,
      "name": "Business"
    },
    {
      "id": 5,
      "name": "Agriculture"
    },
    {
      "id": 6,
      "name": "Dependent"
    }
  ],
  "annualIncome": [
    {
      "id": 1,
      "name": "Rs. 0.00 lacs to Rs. 2.00 Lacs"
    },
    {
      "id": 2,
      "name": "Rs. 2.00 Lacs to Rs. 5 Lacs"
    },
    {
      "id": 3,
      "name": "Rs. 5 Lacs to Rs. 10 Lacs"
    },
    {
      "id": 4,
      "name": "More than Rs. 10 Lacs"
    }
  ],
  "senderProofType": [
    {
      "id": "Aadhaar Card",
      "name": "Aadhaar Card"
    }
  ]
}
    """

    private const val STATIC_SERVICE_DATA = """
        {
						"status": 1,
						"warning": [
							"You want to do Activation Process For IndoNepal Money Transfer.This is chargeable Service.You have to pay 100Rs(including Gst) for this Service after complete the process"
						],
						"note": [
							"Your Csp Code is:-write here user id.",
							"Please download the application form through download link.",
							"Please fill all mandatory fields and upload in PDF format.",
							"Send the application form along with self-attested Aadhar Card, PAN Card to the below mentioned address.",
							"Send all the three documents to - EXCEL ONE STOP SOLUTION PVT. LTD. Granthlok, 5A, 1st Floor Wing A, Taj Hari Hotal, New Pali Road, Near Bhagat ki Kothi Police Chowki, Jodhpur-342005.",
							"Activation will be done within 7-10 working days after receiving the above documents."
						],
						"image": {
							"form": "https://partners.a2zsuvidhaa.com/indoNepal_Onboarding_form/form.pdf",
							"sampleForm": "https://partners.a2zsuvidhaa.com/indoNepal_Onboarding_form/sample.pdf"
						}
					}
    """
    fun staticData(): INStaticData {
        return Gson().fromJson(STATIC_RAW_DATA, INStaticData::class.java)
    }

    fun staticServiceData(): INStaticServiceData {
        return Gson().fromJson(STATIC_SERVICE_DATA, INStaticServiceData::class.java)
    }
}