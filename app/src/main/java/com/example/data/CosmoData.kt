package com.example.data

import com.example.R

object CosmoData {

    val branches = listOf(
        Branch(
            id = "sheraton",
            titleAr = "فرع الشيراتون (الرئيسي)",
            titleEn = "Sheraton Main Branch",
            addressAr = "11 شارع الأديب علي أدهم، شيراتون المطار، النزهة، القاهرة",
            addressEn = "11 Al Adeeb Ali Adham St, Sheraton Al Matar, El Nozha, Cairo",
            phone = "+201206666390",
            email = "sheraton@cairocosmo.online",
            openHoursAr = "يوميًا من 10:00 ص إلى 10:00 م",
            openHoursEn = "Daily 10:00 AM – 10:00 PM",
            mapQuery = "Cairo Cosmo Center Sheraton"
        ),
        Branch(
            id = "dokki",
            titleAr = "فرع الدقي",
            titleEn = "Dokki Branch",
            addressAr = "7 شارع السد العالي، الدقي، الجيزة",
            addressEn = "7 El-Sadd El-Aaly St, Dokki, Giza",
            phone = "+201206666391",
            email = "dokki@cairocosmo.online",
            openHoursAr = "يوميًا من 11:00 ص إلى 10:00 م",
            openHoursEn = "Daily 11:00 AM – 10:00 PM",
            mapQuery = "Dokki Giza Cairo Cosmo Center"
        ),
        Branch(
            id = "october",
            titleAr = "فرع 6 أكتوبر",
            titleEn = "6th of October Branch",
            addressAr = "30 المحور المركزي، السادس من أكتوبر، الجيزة",
            addressEn = "30 Central Axis, 6th of October City, Giza",
            phone = "+201206666392",
            email = "6oct@cairocosmo.online",
            openHoursAr = "يوميًا من 10:00 ص إلى 10:00 م",
            openHoursEn = "Daily 10:00 AM – 10:00 PM",
            mapQuery = "6th of October Cairo Cosmo Center"
        ),
        Branch(
            id = "mansoura",
            titleAr = "فرع المنصورة",
            titleEn = "Mansoura Branch",
            addressAr = "8 شارع الجمهورية، المنصورة، الدقهلية",
            addressEn = "8 El Gomhoureya St, Mansoura, Dakahlia",
            phone = "+201206666393",
            email = "mansoura@cairocosmo.online",
            openHoursAr = "السبت - الخميس: 11:00 ص إلى 9:00 م",
            openHoursEn = "Sat – Thu: 11:00 AM – 9:00 PM",
            mapQuery = "Mansoura Cairo Cosmo Center"
        ),
        Branch(
            id = "shubra",
            titleAr = "فرع شبرا",
            titleEn = "Shubra Branch",
            addressAr = "145 شارع شبرا، القاهرة",
            addressEn = "145 Shubra St, Cairo",
            phone = "+201206666394",
            email = "shubra@cairocosmo.online",
            openHoursAr = "يوميًا من 12:00 م إلى 10:00 م",
            openHoursEn = "Daily 12:00 PM – 10:00 PM",
            mapQuery = "Shubra Cairo Cosmo Center"
        )
    )

    val doctors = listOf(
        Doctor(
            id = "dr-basim-zaki",
            slug = "dr-basim-zaki",
            nameAr = "أ.د. باسم زكي",
            nameEn = "Prof. Dr. Basim Zaki",
            titleAr = "أستاذ ورئيس قسم جراحة التجميل - كلية الطب جامعة عين شمس",
            titleEn = "Professor & Head of Plastic Surgery, Ain Shams University",
            bioAr = "رائد جراحة التجميل ونحت القوام وتجميل الأنف في مصر والشرق الأوسط، عضو الجمعية الدولية لجراحي التجميل (ISAPS) وخبرة أكاديمية وعلاجية تتجاوز 25 عامًا.",
            bioEn = "Pioneer of aesthetic surgery and body contouring in Egypt and the Middle East, member of ISAPS with over 25 years of academic and clinical excellence.",
            experienceAr = "+25 سنة خبرة",
            experienceEn = "25+ Years Experience",
            workingDaysAr = "السبت، الإثنين، الأربعاء (1:00 م - 8:00 م)",
            workingDaysEn = "Sat, Mon, Wed (1:00 PM – 8:00 PM)",
            imageResId = R.drawable.doc_basim_zaki,
            rating = 4.95f,
            reviewCount = 428,
            branches = listOf("sheraton", "dokki"),
            serviceIds = listOf("rhinoplasty", "body-contouring", "facelift", "botox", "fillers")
        ),
        Doctor(
            id = "dr-ahmed-alshrif",
            slug = "dr-ahmed-alshrif",
            nameAr = "أ.د. أحمد الشريف",
            nameEn = "Prof. Dr. Ahmed El Sherif",
            titleAr = "استشاري جراحة التجميل والليزر - كلية الطب جامعة القاهرة",
            titleEn = "Consultant of Plastic Surgery & Laser, Cairo University",
            bioAr = "استشاري جراحة التجميل وتنسيق القوام والليزر التجميلي بقصر العيني، متخصص في أحدث تقنيات شفط الدهون عالي الدقة (VASER) والخيوط الفرنسية.",
            bioEn = "Cosmetic surgery and laser specialist at Cairo University, expert in high-definition VASER liposuction and premium thread lifting.",
            experienceAr = "+22 سنة خبرة",
            experienceEn = "22+ Years Experience",
            workingDaysAr = "الأحد، الثلاثاء، الخميس (12:00 م - 7:00 م)",
            workingDaysEn = "Sun, Tue, Thu (12:00 PM – 7:00 PM)",
            imageResId = R.drawable.doc_ahmed_alshrif,
            rating = 4.92f,
            reviewCount = 376,
            branches = listOf("sheraton", "october", "mansoura"),
            serviceIds = listOf("laser-hair-removal", "body-contouring", "thread-lift", "fillers")
        ),
        Doctor(
            id = "dr-nahid-samir",
            slug = "dr-nahid-samir",
            nameAr = "أ.د. ناهد سمير",
            nameEn = "Prof. Dr. Nahed Samir",
            titleAr = "استشاري الأمراض الجلدية والعلاج بالليزر والحقن التجميلي",
            titleEn = "Consultant of Dermatology, Laser & Aesthetic Injectables",
            bioAr = "أستاذة الجلدية والتجميل بجامعة عين شمس، متخصصة في نضارة البشرة، الفيلر الروسي، البوتوكس العلاجي، وعلاج التصبغات والندبات بالليزر.",
            bioEn = "Professor of Dermatology at Ain Shams University, specialist in skin rejuvenation, Russian lip filler, clinical botox, and pigment laser therapy.",
            experienceAr = "+24 سنة خبرة",
            experienceEn = "24+ Years Experience",
            workingDaysAr = "السبت، الإثنين، الأربعاء (2:00 م - 9:00 م)",
            workingDaysEn = "Sat, Mon, Wed (2:00 PM – 9:00 PM)",
            imageResId = R.drawable.doc_nahid_samir,
            rating = 4.96f,
            reviewCount = 512,
            branches = listOf("sheraton", "dokki", "shubra"),
            serviceIds = listOf("botox", "fillers", "hydrafacial", "chemical-peels", "microneedling")
        )
    )

    val services = listOf(
        ServiceItem(
            id = "botox",
            slug = "botox",
            category = ServiceCategory.INJECTABLES,
            titleAr = "حقن البوتوكس التجميلي والعلاجي",
            titleEn = "Cosmetic & Therapeutic Botox",
            shortDescAr = "إخفاء تجاعيد الجبهة، حول العينين، وعلاج الابتسامة اللثوية والتعرق المفرط بأعلى دقة وطبيعية.",
            shortDescEn = "Smooth forehead lines, crow's feet, treat gummy smile and hyperhidrosis with natural results.",
            fullDescAr = "حقن البوتوكس المعتمد من هيئة الدواء الأمريكية (FDA) في مركز القاهرة الطبي يوفر نتائج شبابية فورية دون تغيير في تعبيرات الوجه الطبيعية. ينفذه أساتذة واستشاريون لضمان التماثل التام والأمان التام.",
            fullDescEn = "FDA-approved Botox injections administered exclusively by medical professors to soften facial wrinkles while preserving natural facial expressions.",
            highlightsAr = listOf("بوتوكس أصلي معتمد من Allergan", "نتائج طبيعية تدوم 4-6 أشهر", "إجراء سريع خلال 15 دقيقة بدون جراحة", "علاج فعال للتعرق الزائد وصداع الشقيقة"),
            highlightsEn = listOf("Genuine Allergan Botox certified", "Natural long-lasting results 4-6 months", "15-minute lunchtime procedure", "Effective hyperhidrosis & migraine treatment"),
            idealForAr = listOf("تجاعيد الجبهة والعبوس", "خطوط زوايا العينين (Crow's Feet)", "الابتسامة اللثوية", "فرط التعرق بالكفين والإبطين"),
            idealForEn = listOf("Forehead & frown lines", "Crow's feet around eyes", "Gummy smile correction", "Excessive sweating (hyperhidrosis)"),
            stepsAr = listOf("فحص تعبيرات الوجه وتحديد نقاط الحقن", "تطبيق كريم مخدر موضعي مهدئ", "حقن دقيق بإبر ميكرو متناهية الصغر", "تعليمات ما بعد الحقن وموعد المتابعة المجانية"),
            stepsEn = listOf("Facial mapping and dynamic analysis", "Topical soothing numbing cream", "Micro-precision injection", "Aftercare instructions & complimentary follow-up"),
            aftercareAr = listOf("عدم الاستلقاء لمدة 4 ساعات بعد الجلسة", "تجنب فرك أو تدليك منطقة الحقن", "الامتناع عن الساونا والرياضة العنيفة ليوم واحد"),
            aftercareEn = listOf("Stay upright for 4 hours after injection", "Avoid massaging the treated area", "Avoid strenuous exercise & saunas for 24h"),
            durationAr = "15 - 20 دقيقة",
            durationEn = "15 – 20 mins",
            startingPriceEgp = 3200,
            doctorIds = listOf("dr-basim-zaki", "dr-nahid-samir"),
            imageResId = R.drawable.svc_botox,
            faqs = listOf(
                ServiceFaq(
                    questionAr = "متى تبدأ نتائج البوتوكس في الظهور؟",
                    questionEn = "When will I see the Botox results?",
                    answerAr = "يبدأ التحسن بالظهور بعد 3 إلى 5 أيام، وتكتمل النتيجة النهائية الرائعة خلال 10 إلى 14 يومًا.",
                    answerEn = "Initial results appear in 3-5 days, with full optimal results visible in 10-14 days."
                ),
                ServiceFaq(
                    questionAr = "هل يسبب البوتوكس جمودًا في الوجه؟",
                    questionEn = "Will Botox make my face look frozen?",
                    answerAr = "لا، يعتمد أطباؤنا على تقنية البوتوكس الطبيعي (Baby Botox) للحفاظ على حركة الوجه التعبيرية الحيوية.",
                    answerEn = "Not at all. Our professors use micro-dosing techniques ensuring vibrant and natural facial movement."
                )
            )
        ),
        ServiceItem(
            id = "fillers",
            slug = "fillers",
            category = ServiceCategory.INJECTABLES,
            titleAr = "حقن الفيلر وحمض الهيالورونيك",
            titleEn = "Dermal Fillers & Hyaluronic Acid",
            shortDescAr = "تحديد الشفاه بالتقنية الروسية، تعبئة الخدود والفك (Texas)، وتصحيح الهالات الغائرة.",
            shortDescEn = "Russian lips technique, cheek volume, Texas jawline contouring, and tear trough correction.",
            fullDescAr = "استعادة امتلاء ومرونة الوجه باستخدام أفخر ماركات الفيلر السويسرية والأمريكية (Juvederm, Restylane, Teosyal). نهدف دائمًا للتناسق الجمالي الراقي المتوافق مع مقاييس الجمال الذهبية.",
            fullDescEn = "Restore facial contour and youthful volume using certified Swiss and American hyaluronic acid fillers.",
            highlightsAr = listOf("فيلر أوروبي معتمد بنقاء 100%", "شفاه روسية مرسومة طبيعية", "تحديد الفك والذقن (Texas Jawline)", "نتائج فورية تستمر حتى 12-18 شهرًا"),
            highlightsEn = listOf("Premium European FDA-certified fillers", "Sculpted Russian lips", "Texas jawline & chin definition", "Immediate results lasting 12-18 months"),
            idealForAr = listOf("ترقق الشفاه وفقدان الحجم", "هالات وتجويف تحت العين", "نحت خط الفك واللغلوغ", "خطوط الابتسامة العميقة حول الفم"),
            idealForEn = listOf("Thin lips needing definition", "Under-eye hollows & dark circles", "Jawline contouring", "Deep nasolabial folds"),
            stepsAr = listOf("استشارة طبية وتحديد الأنسب لملامحك", "تخدير موضعي مريح", "حقن دقيق بالكانيولا لتقليل الكدمات", "تقييم النتيجة الفورية"),
            stepsEn = listOf("Aesthetic consultation & golden ratio mapping", "Topical numbing for comfort", "Blunt cannula injection minimizing bruising", "Immediate review and aftercare"),
            aftercareAr = listOf("تجنب وضع مساحيق التجميل لمدة 24 ساعة", "شرب كميات وافرة من الماء لتثبيت الهيالورونيك", "تجنب حمامات البخار أو الحرارة العالية 48 ساعة"),
            aftercareEn = listOf("Avoid makeup for 24 hours", "Drink plenty of water to hydrate HA", "Avoid saunas and intense heat for 48 hours"),
            durationAr = "30 - 45 دقيقة",
            durationEn = "30 – 45 mins",
            startingPriceEgp = 4500,
            doctorIds = listOf("dr-basim-zaki", "dr-ahmed-alshrif", "dr-nahid-samir"),
            imageResId = R.drawable.svc_fillers,
            faqs = listOf(
                ServiceFaq(
                    questionAr = "هل حقن الفيلر مؤلم؟",
                    questionEn = "Is filler injection painful?",
                    answerAr = "يحتوي الفيلر المستخدم على مخدر الليدوكائين المدمج، بالإضافة لوضع كريم تخدير موضعي لتجربة خالية تمامًا من الألم.",
                    answerEn = "Our fillers contain integrated lidocaine and topical anesthetic is applied, making it virtually painless."
                )
            )
        ),
        ServiceItem(
            id = "laser-hair-removal",
            slug = "laser-hair-removal",
            category = ServiceCategory.LASER,
            titleAr = "ليزر إزالة الشعر بأحدث الأجهزة",
            titleEn = "Advanced Laser Hair Removal",
            shortDescAr = "أحدث أجهزة الكانديلا والجنتل ليز مع تبريد ديناميكي لراحة تامة وأمان لجميع ألوان البشرة.",
            shortDescEn = "Candela GentleLase with dynamic cooling for comfortable, safe treatment on all skin types.",
            fullDescAr = "يقدم مركز القاهرة الطبي بروتوكولات ليزر متطورة بواسطة طاقم تمريضي مدرب تحت إشراف أطباء الجلدية. نعتمد على أجهزة الاندياج والألكسندرايت لنتائج نهائية تدوم ونعومة فائقة خالية من الشعر تحت الجلد.",
            fullDescEn = "State-of-the-art dual wavelength laser technology (Alexandrite & Nd:YAG) safely delivering permanent hair reduction.",
            highlightsAr = listOf("أجهزة Candela GentleLase Pro الأصلية", "نظام تبريد فوري DCD لتجربة غير مؤلمة", "مناسب للبشرة الفاتحة والسمراء", "جلسات رتوش مجانية متابعة"),
            highlightsEn = listOf("Original Candela GentleLase Pro machines", "DCD dynamic cooling for painless sessions", "Safe for all Fitzpatrick skin types", "Complimentary touch-up sessions"),
            idealForAr = listOf("إزالة الشعر الدائم للجسم والوجه", "علاج جلد الوزة والشعر تحت الجلد", "تفتيح وتنعيم المناطق الحساسة"),
            idealForEn = listOf("Permanent hair reduction for body & face", "Treating strawberry legs and ingrown hair", "Skin smoothing and tone enhancement"),
            stepsAr = listOf("كشف نوع الجلد ولون الشعرة بجهاز التحليل", "ضبط المعايير والطاقة المناسبة", "إجراء النبضات مع التبريد المزدوج", "وضع كريم مهدئ ضد التحسس"),
            stepsEn = listOf("Skin and hair melanin scanning", "Calibrating energy parameters", "Laser delivery with dynamic cooling", "Post-treatment calming cream"),
            aftercareAr = listOf("استخدام واقي شمس مناسب", "ترطيب البشرة بالكريمات الموصوفة", "الابتعاد عن المياه الساخنة لمدة 24 ساعة"),
            aftercareEn = listOf("Apply SPF 50+ sunscreen", "Moisturize with recommended lotion", "Avoid hot baths for 24 hours"),
            durationAr = "20 - 60 دقيقة",
            durationEn = "20 – 60 mins",
            startingPriceEgp = 1500,
            doctorIds = listOf("dr-ahmed-alshrif", "dr-nahid-samir"),
            imageResId = R.drawable.svc_laser,
            faqs = listOf(
                ServiceFaq(
                    questionAr = "كم عدد الجلسات المطلوبة لإزالة الشعر نهائياً؟",
                    questionEn = "How many sessions are typically required?",
                    answerAr = "يحتاج معظم الأشخاص من 6 إلى 8 جلسات بفواصل شهرية للوصول إلى نسبة اختفاء تتجاوز 90%.",
                    answerEn = "Most patients require 6 to 8 sessions spaced monthly to achieve over 90% permanent reduction."
                )
            )
        ),
        ServiceItem(
            id = "hydrafacial",
            slug = "hydrafacial",
            category = ServiceCategory.SKIN,
            titleAr = "تنظيف ونضارة البشرة الهيدرافيشل",
            titleEn = "HydraFacial Glow & Rejuvenation",
            shortDescAr = "تنظيف عميق، تقشير، استخراج الرؤوس السوداء، وترطيب مكثف بمضادات الأكسدة والببتيدات.",
            shortDescEn = "Deep vortex cleansing, gentle peel, blackhead extraction, and intense peptide hydration.",
            fullDescAr = "علاج متكامل 6 في 1 يمنحك بشرة نضرة ومشرقة من الجلسة الأولى دون أي فترة نقاهة. مناسب قبل المناسبات والأعراس لإشراقة ملكية فورية.",
            fullDescEn = "The premier 6-in-1 medical facial delivering instant, radiant skin with zero downtime.",
            highlightsAr = listOf("تقنية Vortex-Fusion الأصلية", "إزالة الرؤوس السوداء وتضييق المسام", "توريد وترطيب بالهيالورونيك", "نضارة ملحوظة من أول جلسة"),
            highlightsEn = listOf("Patented Vortex-Fusion technology", "Extracts blackheads & refines pores", "Hyaluronic and peptide infusion", "Instant visible glow"),
            idealForAr = listOf("البشرة الباهتة والمجهدة", "انسداد المسام وتراكم الدهون", "الخطوط الدقيقة وجفاف البشرة", "تجهيز العروس قبل الزفاف"),
            idealForEn = listOf("Dull, tired complexion", "Clogged pores and excess sebum", "Fine lines & dehydration", "Pre-event instant bridal glow"),
            stepsAr = listOf("تنظيف وتقشير أولي للبشرة", "تقشير حمضي لطيف لتفتيت الرواسب", "شفط الرؤوس السوداء بالضغط الفراغي", "تغذية السيرومات وترطيب الـ LED"),
            stepsEn = listOf("Cleanse and gentle exfoliation", "Mild acid peel softening impurities", "Vortex vacuum extraction", "Antioxidant infusion & LED light therapy"),
            aftercareAr = listOf("عدم غسل الوجه لمدة 4 ساعات للاستفادة من السيرومات", "وضع واقي الشمس", "تجنب المكياج لبقية اليوم"),
            aftercareEn = listOf("Leave serums on for 4 hours", "Apply SPF 50 daily", "Avoid heavy makeup for 24 hours"),
            durationAr = "45 - 60 دقيقة",
            durationEn = "45 – 60 mins",
            startingPriceEgp = 1800,
            doctorIds = listOf("dr-nahid-samir"),
            imageResId = R.drawable.svc_rejuvenation,
            faqs = listOf(
                ServiceFaq(
                    questionAr = "هل توجد فترة نقاهة بعد جلسة الهيدرافيشل؟",
                    questionEn = "Is there downtime after HydraFacial?",
                    answerAr = "إطلاقاً، يمكنك ممارسة حياتك الطبيعية فوراً مع إشراقة ساحرة بدون أي احمرار.",
                    answerEn = "Zero downtime! You can return to normal activities immediately with glowing skin."
                )
            )
        ),
        ServiceItem(
            id = "chemical-peels",
            slug = "chemical-peels",
            category = ServiceCategory.SKIN,
            titleAr = "التقشير الكيميائي والبارد (Dermamelan)",
            titleEn = "Chemical & Cold Peeling (Dermamelan)",
            shortDescAr = "علاج الكلف المستعصي، تفتيح التصبغات وآثار حب الشباب وتجديد خلايا البشرة.",
            shortDescEn = "Treat stubborn melasma, lighten hyperpigmentation, acne scars, and renew dermal layers.",
            fullDescAr = "أحدث بروتوكولات التقشير الطبي الآمن (التقشير البارد، أحماض الفواكه، TCA) المخصصة لكل حالة بإشراف نخبة من أساتذة الجلدية للحصول على بشرة موحدة وخالية من البقع.",
            fullDescEn = "Advanced chemical and depigmenting peeling protocols targeting stubborn melasma and post-inflammatory pigmentation.",
            highlightsAr = listOf("أقنعة تقشير بارد إسبانية أصلية", "علاج فعال للكلف والتصبغات الهرمونية", "تجديد الكولاجين وتحسين ملمس الجلد", "متابعة دورية مستمرة"),
            highlightsEn = listOf("Genuine Spanish depigmentation masks", "Targeted melasma and pigment therapy", "Stimulates collagen regeneration", "Continuous dermatological follow-up"),
            idealForAr = listOf("كلف الحمل والتصبغات العميقة", "آثار وندبات حب الشباب", "عدم توحد لون الوجه والرقبة"),
            idealForEn = listOf("Hormonal melasma & sun spots", "Post-acne blemishes and marks", "Uneven complexion and dullness"),
            stepsAr = listOf("تنظيف طبي شامل للبشرة", "وضع المحلول المقشر بدرجة محددة", "تثبيت قناع التفتيح الطبي", "خطة الكريمات المنزلية الداعمة"),
            stepsEn = listOf("Skin preparation and degreasing", "Controlled peel application", "Soothing therapeutic mask", "Home maintenance prescription"),
            aftercareAr = listOf("الالتزام التام بكريمات الترطيب وواقي الشمس", "عدم محاولة نزع القشور يدويًا", "تجنب التعرض المباشر للحرارة والطهي"),
            aftercareEn = listOf("Apply prescribed healing creams and SPF", "Never pick peeling skin manually", "Avoid heat sources and direct sunlight"),
            durationAr = "30 دقيقة",
            durationEn = "30 mins",
            startingPriceEgp = 2500,
            doctorIds = listOf("dr-nahid-samir"),
            imageResId = R.drawable.svc_peels,
            faqs = listOf(
                ServiceFaq(
                    questionAr = "متى يبدأ تقشير الجلد بعد الجلسة؟",
                    questionEn = "When does the peeling process start?",
                    answerAr = "يبدأ التقشير في اليوم الثالث ويستمر قرابة 5-7 أيام لتظهر بشرة نضرة وموحدة تمامًا.",
                    answerEn = "Peeling begins on day 3 and continues for 5-7 days, revealing fresh uniform skin."
                )
            )
        ),
        ServiceItem(
            id = "body-contouring",
            slug = "body-contouring",
            category = ServiceCategory.BODY,
            titleAr = "نحت القوام وتنسيق القوام (VASER & HIFU)",
            titleEn = "Body Contouring & High-Def Sculpting",
            shortDescAr = "نحت عضلات البطن، شد ترهلات الذراعين والفخذين، وشفط الدهون عالي الدقة بدون جراحة معقدة.",
            shortDescEn = "High-definition sculpting, tightening laxity, and targeted fat reduction with VASER.",
            fullDescAr = "إجراءات نحت القوام الأحدث في مصر على يد أ.د. باسم زكي وأ.د. أحمد الشريف باستخدام أحدث أجهزة الليزر والفيزر والهايفو لنتائج قوام ممشوق ومثالي.",
            fullDescEn = "Advanced body contouring and fat emulsification using modern VASER and ultrasound technology led by academic professors.",
            highlightsAr = listOf("استشارات يقودها كبار أساتذة جراحة التجميل", "أحدث تقنيات الفيزر رباعي الأبعاد (4D VASER)", "شد الجلد بدون ندبات ظاهرة", "فترة تعافي سريعة ونتائج دائمة"),
            highlightsEn = listOf("Consultations led by senior university chairs", "Latest 4D VASER sculpting technology", "Skin tightening without scars", "Fast recovery with permanent contours"),
            idealForAr = listOf("الدهون الموضعية المقاومة للدايت والرياضة", "ترهلات ما بعد الولادة أو نزول الوزن", "تحديد عضلات البطن والخصر"),
            idealForEn = listOf("Diet-resistant stubborn fat pockets", "Post-pregnancy or weight loss laxity", "Waistline & athletic abdominal sculpting"),
            stepsAr = listOf("تقييم تشريحي شامل وتحديد الأهداف", "اختيار الإجراء الجراحي أو غير الجراحي", "خطة العمليات والمتابعة الخاصة", "برنامج المشدات والتصريف اللمفاوي"),
            stepsEn = listOf("Comprehensive body assessment", "Tailoring surgical vs non-invasive path", "Procedure execution with hospital privileges", "Post-care compression & lymphatic follow-up"),
            aftercareAr = listOf("ارتداء المشد الطبي بالفترة المحددة", "الالتزام بجلسات المساج اللمفاوي", "شرب السوائل وممارسة المشي الخفيف"),
            aftercareEn = listOf("Wear compression garment as advised", "Lymphatic drainage sessions", "Stay hydrated and engage in light walking"),
            durationAr = "ساعة - ساعتان",
            durationEn = "1 – 2 hours",
            startingPriceEgp = 9500,
            doctorIds = listOf("dr-basim-zaki", "dr-ahmed-alshrif"),
            imageResId = R.drawable.svc_dermatology,
            faqs = listOf(
                ServiceFaq(
                    questionAr = "هل تعود الدهون بعد عملية نحت القوام؟",
                    questionEn = "Will fat return after body sculpting?",
                    answerAr = "الخلايا الدهنية التي يتم استئصالها أو تكسيرها لا تعود مجددًا ما دام الوزن العام مستقرًا.",
                    answerEn = "Eliminated fat cells do not regenerate provided you maintain a stable healthy lifestyle."
                )
            )
        )
    )

    val offers = listOf(
        Offer(
            id = "offer-full-face-glow",
            titleAr = "باقة النضارة الملكية (بوتوكس + هيدرافيشل)",
            titleEn = "Royal Glow Duo (Botox + HydraFacial)",
            descAr = "جلسة بوتوكس كامل الجبهة والعيون مع جلسة تنظيف هيدرافيشل ديب مع سيروم الذهب والنضارة.",
            descEn = "Full upper face Botox combined with deep HydraFacial glow infused with gold peptide serum.",
            discountPercent = 25,
            originalPriceEgp = 5000,
            offerPriceEgp = 3750,
            promoCode = "ROYAL25",
            category = ServiceCategory.INJECTABLES,
            expiresAr = "عرض متاح لـ 50 حجز هذا الأسبوع",
            expiresEn = "Available for first 50 bookings this week"
        ),
        Offer(
            id = "offer-laser-full-body",
            titleAr = "باكيدج ليزر الجسم كامل 6 جلسات + رتوش",
            titleEn = "Full Body Laser 6 Sessions + Touch-ups",
            descAr = "إزالة شعر الجسم كامل مع جلسات رتوش مجانية بعد كل جلسة بأجهزة كانديلا الأصلية.",
            descEn = "Complete full body laser hair reduction with free touch-ups on authentic Candela machines.",
            discountPercent = 35,
            originalPriceEgp = 12000,
            offerPriceEgp = 7800,
            promoCode = "LASER35",
            category = ServiceCategory.LASER,
            expiresAr = "صالح حتى نهاية الشهر",
            expiresEn = "Valid until end of the month"
        ),
        Offer(
            id = "offer-lips-russian",
            titleAr = "عرض الفيلر السويسري وتحديد الشفاه",
            titleEn = "Swiss Lip Filler & Contouring",
            descAr = "1 مل فيلر سويسري أصلي مع تقنية التوريد الروسي واستشارة دكتورة ناهد سمير.",
            descEn = "1 ml authentic Swiss hyaluronic filler with Russian contouring by Prof. Nahed Samir.",
            discountPercent = 20,
            originalPriceEgp = 5500,
            offerPriceEgp = 4400,
            promoCode = "LIPS20",
            category = ServiceCategory.INJECTABLES,
            expiresAr = "ساري بفروع الشيراتون والدقي",
            expiresEn = "Valid at Sheraton & Dokki branches"
        ),
        Offer(
            id = "offer-contouring-consult",
            titleAr = "كشف وتخطيط نحت القوام مع أ.د. باسم زكي",
            titleEn = "Body Sculpting Consult with Prof. Basim Zaki",
            descAr = "فحص 3D كامل وتخطيط الإجراء الجراحي أو الفيزر مع خصم 15% على إجراءات العمليات.",
            descEn = "Full 3D anatomical evaluation with 15% VIP discount on hospital surgical procedures.",
            discountPercent = 15,
            originalPriceEgp = 2000,
            offerPriceEgp = 1200,
            promoCode = "SCULPT15",
            category = ServiceCategory.BODY,
            expiresAr = "محدود بـ 15 استشارة أسبوعيًا",
            expiresEn = "Limited to 15 consults weekly"
        )
    )

    val testimonials = listOf(
        Testimonial(
            id = "t1",
            patientNameAr = "سارة محمود",
            patientNameEn = "Sarah Mahmoud",
            treatmentAr = "حقن بوتوكس وفيلر شفايف",
            treatmentEn = "Botox & Russian Lip Filler",
            commentAr = "تجربتي مع دكتورة ناهد سمير في فرع الشيراتون كانت ممتازة! النتيجة طبيعية جداً وبدون أي تورم، الكل بيسألني عن سر نضارة وجهي.",
            commentEn = "My experience with Prof. Nahed at Sheraton branch was sensational! The results look so natural and no swelling at all.",
            rating = 5,
            verified = true
        ),
        Testimonial(
            id = "t2",
            patientNameAr = "م. كريم عبد الرحمن",
            patientNameEn = "Eng. Karim Abdelrahman",
            treatmentAr = "نحت القوام بالفيزر",
            treatmentEn = "VASER Liposuction",
            commentAr = "أ.د. باسم زكي قامة علمية وإنسانية كبيرة، العملية غيرت مظهري تماماً وزادت ثقتي بنفسي، الاهتمام والمتابعة بعد العملية كانا في قمة الرقي.",
            commentEn = "Prof. Basim Zaki is an absolute maestro in plastic surgery. The outcome exceeded my expectations and aftercare was world-class.",
            rating = 5,
            verified = true
        ),
        Testimonial(
            id = "t3",
            patientNameAr = "يارا النجار",
            patientNameEn = "Yara El Naggar",
            treatmentAr = "كورس ليزر كانديلا 6 جلسات",
            treatmentEn = "6 Candela Laser Sessions",
            commentAr = "جهاز الكانديلا بارد ومفيش أي ألم، والتمريض في فرع الدقي على أعلى مستوى من النظافة والاحترافية. الشعر اختفى من الجلسة الرابعة تقريباً.",
            commentEn = "The laser device cooling makes it pain-free, Dokki team is very hygienic and professional. Hair practically disappeared by session 4.",
            rating = 5,
            verified = true
        )
    )

    val calculatorOptions = listOf(
        TreatmentCalculatorOption(
            id = "calc-botox-upper",
            nameAr = "بوتوكس الجبهة ومحيط العينين",
            nameEn = "Forehead & Eye Area Botox",
            basePriceEgp = 3200,
            defaultSessions = 1,
            intervalWeeks = 20,
            category = ServiceCategory.INJECTABLES,
            benefitsAr = listOf("إخفاء الخطوط التعبيرية", "رفع خفيف للحواجب", "نتيجة خلال 5 أيام"),
            benefitsEn = listOf("Erases dynamic lines", "Subtle brow lift", "Results in 5 days")
        ),
        TreatmentCalculatorOption(
            id = "calc-filler-contour",
            nameAr = "فيلر الخدود وتحديد الفك والشفاه",
            nameEn = "Cheek, Lip & Jawline Filler",
            basePriceEgp = 4500,
            defaultSessions = 2,
            intervalWeeks = 40,
            category = ServiceCategory.INJECTABLES,
            benefitsAr = listOf("استعادة امتلاء الوجه", "تحديد خط الفك بزاوية راقية", "يدوم حتى سنة ونصف"),
            benefitsEn = listOf("Restores facial volume", "Sharp Texas jawline", "Lasts up to 18 months")
        ),
        TreatmentCalculatorOption(
            id = "calc-laser-hair",
            nameAr = "ليزر إزالة شعر الجسم كامل",
            nameEn = "Full Body Candela Laser",
            basePriceEgp = 1600,
            defaultSessions = 6,
            intervalWeeks = 4,
            category = ServiceCategory.LASER,
            benefitsAr = listOf("تراجع نمو الشعر بنسبة 95%", "بشرة ناعمة وحريرية", "علاج جلد الوزة"),
            benefitsEn = listOf("95% permanent reduction", "Silk-smooth skin", "Eliminates ingrown hairs")
        ),
        TreatmentCalculatorOption(
            id = "calc-hydrafacial",
            nameAr = "جلسات تنظيف ونضارة هيدرافيشل",
            nameEn = "HydraFacial Glow Sessions",
            basePriceEgp = 1800,
            defaultSessions = 3,
            intervalWeeks = 3,
            category = ServiceCategory.SKIN,
            benefitsAr = listOf("إزالة الرؤوس السوداء والشوائب", "ترطيب وتفتيح فوري", "بدون أي فترة نقاهة"),
            benefitsEn = listOf("Deep pore vacuuming", "Instant radiance boost", "Zero downtime")
        ),
        TreatmentCalculatorOption(
            id = "calc-peels-melasma",
            nameAr = "كورس التقشير البارد للكلف والتفتيح",
            nameEn = "Cold Peeling Melasma Course",
            basePriceEgp = 2500,
            defaultSessions = 2,
            intervalWeeks = 6,
            category = ServiceCategory.SKIN,
            benefitsAr = listOf("تفتيح البقع العميقة", "توحيد لون البشرة", "تحفيز تجديد خلايا الجلد"),
            benefitsEn = listOf("Fades deep pigmentation", "Evens out skin tone", "Accelerates dermal renewal")
        ),
        TreatmentCalculatorOption(
            id = "calc-vaser-body",
            nameAr = "نحت وشد القوام بالفيزر",
            nameEn = "VASER High-Def Body Sculpting",
            basePriceEgp = 9500,
            defaultSessions = 1,
            intervalWeeks = 52,
            category = ServiceCategory.BODY,
            benefitsAr = listOf("تحديد عضلات وخصر مثالي", "نتائج دائمة ومثبتة", "إشراف نخبة أساتذة التجميل"),
            benefitsEn = listOf("Hourglass / athletic contour", "Permanent lasting results", "Supervised by university chairs")
        )
    )

    val initialAppointments = listOf(
        Appointment(
            id = "app-001",
            refCode = "CCC-7842",
            customerName = "مريم الأحمدي",
            customerPhone = "+201012345678",
            customerEmail = "mariam.ahmadi@gmail.com",
            branchId = "sheraton",
            serviceId = "botox",
            doctorId = "dr-nahid-samir",
            date = "2026-11-02",
            timeSlot = "02:30 م",
            notes = "جلسة أولى لبوتوكس الجبهة ومحيط العينين",
            status = AppointmentStatus.CONFIRMED,
            createdAt = "2026-10-28"
        ),
        Appointment(
            id = "app-002",
            refCode = "CCC-7843",
            customerName = "شريف النحاس",
            customerPhone = "+201123456789",
            customerEmail = "sherif.alnahas@yahoo.com",
            branchId = "dokki",
            serviceId = "body-contouring",
            doctorId = "dr-basim-zaki",
            date = "2026-11-04",
            timeSlot = "05:00 م",
            notes = "استشارة نحت قوام بالفيزر وفحص ما بعد الحمية",
            status = AppointmentStatus.PENDING,
            createdAt = "2026-10-29"
        ),
        Appointment(
            id = "app-003",
            refCode = "CCC-7844",
            customerName = "دينا الشاذلي",
            customerPhone = "+201234567890",
            customerEmail = "dina.elshazly@outlook.com",
            branchId = "october",
            serviceId = "laser-hair-removal",
            doctorId = "dr-ahmed-alshrif",
            date = "2026-11-05",
            timeSlot = "12:00 م",
            notes = "جلسة الليزر الثالثة مع متابعة الرتوش",
            status = AppointmentStatus.CONFIRMED,
            createdAt = "2026-10-29"
        )
    )
}
